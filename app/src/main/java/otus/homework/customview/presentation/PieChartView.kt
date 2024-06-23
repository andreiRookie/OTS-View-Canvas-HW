package otus.homework.customview.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import otus.homework.customview.R
import otus.homework.customview.util.TAG
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

class PieChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val sectorPaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        flags = Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG
    }
    private val strokePaint: Paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 2f
        flags = Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG
    }

    private var chartRadius = RADIUS_DEFAULT.dp()

    private var pieChartModel = PieChartModel(emptyList())

    private var onSectorClickListener: OnSectorClickListener? = null

    private val rect = RectF()

    init {
        attrs?.let { initAttrs(it) }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val wModeFromSpec = MeasureSpec.getMode(widthMeasureSpec)
        val hModeFromSpec = MeasureSpec.getMode(heightMeasureSpec)

        val widthFromSpec = MeasureSpec.getSize(widthMeasureSpec)
        val heightFromSpec = MeasureSpec.getSize(heightMeasureSpec)

        val wMeasured = when (wModeFromSpec) {
            MeasureSpec.EXACTLY -> {
                widthFromSpec
            }

            MeasureSpec.AT_MOST -> {
                minOf((chartRadius * 2).toInt(), widthFromSpec)
            }

            else -> {
                (chartRadius * 2).toInt()
            }
        } + paddingLeft + paddingRight

        val hMeasured = when (hModeFromSpec) {
            MeasureSpec.EXACTLY -> {
                heightFromSpec
            }

            MeasureSpec.AT_MOST -> {
                minOf((chartRadius * 2).toInt(), heightFromSpec)
            }

            else -> {
                (chartRadius * 2).toInt()
            }
        } + paddingTop + paddingBottom

        if (wMeasured > hMeasured) {
            setMeasuredDimension(hMeasured, hMeasured)
        } else {
            setMeasuredDimension(wMeasured, wMeasured)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (pieChartModel.sectors.isEmpty()) return

        rect.set(
            0f,
            0f,
            width.toFloat(),
            height.toFloat()
        )

        pieChartModel.sectors.forEach { model ->

            sectorPaint.color = model.color

            canvas.drawArc(rect, model.startAngle, model.sweepAngle, true, sectorPaint)
            canvas.drawArc(rect, model.startAngle, model.sweepAngle, true, strokePaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

                val pieChartCenterPoint = PointF((width / 2).toFloat(), (height / 2).toFloat())

                val vectorCoordinatesPoint =
                    PointF(event.x - pieChartCenterPoint.x, event.y - pieChartCenterPoint.y)

                val vectorLength =
                    sqrt(vectorCoordinatesPoint.x.pow(2) + vectorCoordinatesPoint.y.pow(2))

                val currentRadius = width / 2

                if (vectorLength > currentRadius) return false

                val thetaAngleInRadiance = atan2(vectorCoordinatesPoint.y, vectorCoordinatesPoint.x)
                val thetaAngleInDegrees = Math.toDegrees(thetaAngleInRadiance.toDouble())

                val thetaAngleAsSectorAngle = if (thetaAngleInDegrees < 0) {
                    DEGREES_360 + thetaAngleInDegrees
                } else {
                    thetaAngleInDegrees
                }

                pieChartModel.sectors.forEach { sectorModel ->
                    if (thetaAngleAsSectorAngle > sectorModel.startAngle &&
                        thetaAngleAsSectorAngle <= sectorModel.startAngle + sectorModel.sweepAngle
                    ) {
                        onSectorClickListener?.onSectorClick(
                            CategoryData(
                                sectorModel.categoryName,
                                sectorModel.totalValue,
                                sectorModel.expenseList
                            )
                        )
                    }
                }
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                return true
            }
            MotionEvent.ACTION_UP -> {
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(CHART_STATE, pieChartModel)
        bundle.putParcelable(SUPER_STATE, super.onSaveInstanceState())
        return bundle

//        return ChartState(super.onSaveInstanceState(), pieChartModel)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {

        val restoredState = state as Bundle
        restoredState.getParcelable(CHART_STATE, PieChartModel::class.java)?.let {
            pieChartModel = it
        }
        super.onRestoreInstanceState(
            restoredState.getParcelable(
                SUPER_STATE,
                Parcelable::class.java
            )
        )

//        val chartState = state as ChartState
//        super.onRestoreInstanceState(chartState.superState)
//        pieChartModel = chartState.pieChartModel
    }

    fun setPieChartModel(model: PieChartModel) {
        pieChartModel = model

        requestLayout()
        invalidate()
    }

    fun setSectorClickListener(listener: OnSectorClickListener) {
        onSectorClickListener = listener
    }

    private fun initAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PieChartView)
        try {
            chartRadius =
                typedArray.getDimension(R.styleable.PieChartView_chartRadius, RADIUS_DEFAULT.dp())
        } finally {
            typedArray.recycle()
        }
    }

    private fun Float.dp(): Float {
        return this * context.resources.displayMetrics.density
    }

    companion object {
        private const val RADIUS_DEFAULT = 64f

        private const val DEGREES_360 = 360F

        private const val CHART_STATE = "PieChartView.CHART_STATE"
        private const val SUPER_STATE = "PieChartView.SUPER_STATE"
    }
}