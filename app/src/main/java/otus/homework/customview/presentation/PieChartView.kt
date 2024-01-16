package otus.homework.customview.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import otus.homework.customview.R
import otus.homework.customview.util.TAG

class PieChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
): View(context, attrs) {

    private val paintsFlags = Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG
    private val sectorPaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        flags = paintsFlags
    }
    private val strokePaint: Paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 2f
        flags = paintsFlags
    }

    private var chartRadius = RADIUS_DEFAULT.dp()

    private var pieChartModel = PieChartModel(emptyList())

    private val rect = RectF()

    private val gestureDetector = GestureDetector(context, object :
        GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return super.onDown(e)
        }
    })

    init {
//        if (isInEditMode) {
//            pieChartModel = stubPieChartModel
//        }

        attrs?.let { initAttrs(it) }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val wModeFromSpec = MeasureSpec.getMode(widthMeasureSpec)
        val hModeFromSpec = MeasureSpec.getMode(heightMeasureSpec)

        val widthFromSpec = MeasureSpec.getSize(widthMeasureSpec)
        val heightFromSpec = MeasureSpec.getSize(heightMeasureSpec)

        val wMeasured = when (wModeFromSpec) {
            MeasureSpec.EXACTLY -> {
                println("${this.TAG} EXACTLY $widthFromSpec $heightFromSpec")
                widthFromSpec
            }

            MeasureSpec.AT_MOST -> {
                println("${this.TAG} AT_MOST $widthFromSpec $heightFromSpec")
                minOf((chartRadius * 2).toInt(), widthFromSpec)
            }

            else -> {
                println("${this.TAG} UNSPECIFIED $widthFromSpec $heightFromSpec")
                (chartRadius * 2).toInt()
            }
        } + paddingLeft + paddingRight

        val hMeasured = when (hModeFromSpec) {
            MeasureSpec.EXACTLY -> {
               println("${this.TAG} EXACTLY $widthFromSpec $heightFromSpec")
                heightFromSpec
            }

            MeasureSpec.AT_MOST -> {
                println("${this.TAG} AT_MOST $widthFromSpec $heightFromSpec")
                minOf((chartRadius * 2).toInt(),  heightFromSpec)
            }

            else -> {
                println("${this.TAG} UNSPECIFIED $widthFromSpec $heightFromSpec")
                (chartRadius * 2).toInt()
            }
        } + paddingTop + paddingBottom

        println("${this.TAG} MEASURED DIMENSIONS $wMeasured $hMeasured")
        if (wMeasured > hMeasured) {
            setMeasuredDimension(hMeasured, hMeasured)
        } else {
            setMeasuredDimension(wMeasured, wMeasured)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (pieChartModel.sectors.isEmpty()) return

        println("${this.TAG} onDraw width $width height $height")

        rect.set(0f,
            0f,
            width.toFloat(),
            height.toFloat())

        var startAngle = 0f

        pieChartModel.sectors.forEach { model ->

            sectorPaint.color = model.color

            canvas.drawArc(rect, startAngle, model.sweepAngle, true, sectorPaint)
            canvas.drawArc(rect, startAngle, model.sweepAngle, true, strokePaint)

            startAngle += model.sweepAngle
        }
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
        super.onRestoreInstanceState(restoredState.getParcelable(SUPER_STATE, Parcelable::class.java))

//        val chartState = state as ChartState
//        super.onRestoreInstanceState(chartState.superState)
//        pieChartModel = chartState.pieChartModel
    }

    fun setPieChartModel(model: PieChartModel) {
        pieChartModel = model

        println("${this.TAG} setPieChartModel pieChartModel $pieChartModel")

        requestLayout()
        invalidate()
    }


    private fun initAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PieChartView)
        try {
            chartRadius = typedArray.getDimension(R.styleable.PieChartView_chartRadius, RADIUS_DEFAULT.dp())
        } finally {
            typedArray.recycle()
        }
    }

    private fun Float.dp(): Float {
        return this * context.resources.displayMetrics.density
    }

    companion object {
        private const val RADIUS_DEFAULT = 64f

        private const val CHART_STATE = "PieChartView.CHART_STATE"
        private const val SUPER_STATE = "PieChartView.SUPER_STATE"
    }
}