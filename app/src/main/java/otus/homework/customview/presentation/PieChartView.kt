package otus.homework.customview.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import otus.homework.customview.R
import otus.homework.customview.util.TAG

class PieChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
): View(context, attrs) {

    private val generator = ColorGeneratorImpl()

    private lateinit var sectorPaintList: MutableList<Paint>
    private lateinit var strokePaint: Paint

    private var chartRadius = RADIUS_DEFAULT.dp()

    private var pieChartModel = PieChartModel(emptyList())

    private val rect = RectF()

    init {
        if (isInEditMode) {
            initPaintBrushes()
        }
        attrs?.let {initAttrs(it)}

        initPaintBrushes()
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
        println("${this.TAG} width $width height $height")

        if (pieChartModel.categories.isEmpty()) return

        rect.set(0f,
            0f,
            width.toFloat(),
            height.toFloat())

        var startAngle = 0f
        pieChartModel.categories.forEachIndexed { index, model ->
            canvas.drawArc(rect, startAngle, model.sectorSweepAngle, true, sectorPaintList[index])
            canvas.drawArc(rect, startAngle, model.sectorSweepAngle, true, strokePaint)
            startAngle += model.sectorSweepAngle
        }
    }

    fun setPieChartModel(model: PieChartModel) {
        pieChartModel = model
        requestLayout()
        invalidate()
    }

    fun setChartRadius(radius: Float) {
        chartRadius = radius
    }

    private fun initPaintBrushes() {
        sectorPaintList = mutableListOf()

        val paintsFlags = Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG

        defaultColors.forEach { value ->

                sectorPaintList += Paint().apply {
                    color = value
                    style = Paint.Style.FILL
                    flags = paintsFlags
                }
        }

//        if (pieChartModel.categories.size > defaultColors.size) {
//            val diff = pieChartModel.categories.size - defaultColors.size
//            (0 until diff).forEach {
//                sectorPaintList += Paint().apply {
//                    color = generator.generateColor()
//                    style = Paint.Style.FILL
//                    flags = paintsFlags
//                }
//            }
//        }

        strokePaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 2f
            flags = paintsFlags
        }

        println("${this.TAG} paintList $sectorPaintList")
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

        private val defaultColors = listOf(
            Color.GREEN,
            Color.RED,
            Color.BLUE,
            Color.DKGRAY,
            Color.CYAN,
            Color.YELLOW,
            Color.MAGENTA,
            Color.GRAY,
            Color.WHITE
        )
    }
}