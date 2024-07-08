package otus.homework.customview.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class CategoryDetailsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var data = CategoryDetailsGraphModel(emptyMap())

    private val graphPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 8f
        flags = Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND      // set the paint cap to round too
        pathEffect = (CornerPathEffect(10f)) // set the path effect when they join
    }

    private val axisPaint = Paint().apply {
        color = Color.DKGRAY
        style = Paint.Style.STROKE
        strokeWidth = 4f
        flags = Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG
    }

    private val graphPath = Path()
    private val axisPath = Path()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val wModeFromSpec = MeasureSpec.getMode(widthMeasureSpec)
        val hModeFromSpec = MeasureSpec.getMode(heightMeasureSpec)

        val wFromSpec = MeasureSpec.getSize(widthMeasureSpec)
        val hFromSpec = MeasureSpec.getSize(heightMeasureSpec)

        val wMeasured = when (wModeFromSpec) {
            MeasureSpec.EXACTLY -> {
                wFromSpec
            }

            MeasureSpec.AT_MOST -> {
                minOf(wFromSpec, width)
            }

            else -> {
                width
            }
        } + paddingStart + paddingEnd


        val hMeasured = when (hModeFromSpec) {
            MeasureSpec.EXACTLY -> {
                hFromSpec
            }

            MeasureSpec.AT_MOST -> {
                minOf(hFromSpec, height)
            }

            else -> {
                height
            }
        } + paddingTop + paddingBottom

        setMeasuredDimension(wMeasured, hMeasured)
    }

    override fun onDraw(canvas: Canvas) {
        val startXGap = 8.dp()
        val startYGap = 8.dp()
        val axisPointerArmOffset = 4.dp()

        val measuredWidthWithGap = measuredWidth - startXGap
        val measuredHeightWithGap = measuredHeight - startYGap

        // start
        axisPath.reset()
        axisPath.moveTo(startXGap, 0f)

        // Y axis pointer
        axisPath.lineTo(axisPointerArmOffset, startYGap)
        axisPath.moveTo(startXGap, 0f)
        axisPath.lineTo(startXGap + axisPointerArmOffset, startYGap)

        // axises
        axisPath.moveTo(startXGap, 0f)
        axisPath.lineTo(startXGap, measuredHeightWithGap)
        axisPath.lineTo(measuredWidth.toFloat(), measuredHeightWithGap)

        // X axis pointer
        axisPath.lineTo(measuredWidthWithGap, measuredHeightWithGap - axisPointerArmOffset)
        axisPath.moveTo(measuredWidth.toFloat(), measuredHeightWithGap)
        axisPath.lineTo(measuredWidthWithGap, measuredHeightWithGap + axisPointerArmOffset)

        // X axis day steps
        val firstStepXGap = 32.dp()
        val axisMarkLength = 16.dp()
        val axisXDateMarkCount = data.expensesByDateMap.keys.size
        val axiXStepWidth = (measuredWidthWithGap - firstStepXGap * 2) / axisXDateMarkCount

        var stepsXDistance: Float = startXGap
        repeat(axisXDateMarkCount) { step ->
            if (step == 0) {
                stepsXDistance += firstStepXGap

                // X axis with marks
                axisPath.moveTo(stepsXDistance, measuredHeightWithGap - axisMarkLength / 2)
                axisPath.lineTo(stepsXDistance, measuredHeightWithGap + axisMarkLength)
            } else {
                stepsXDistance += axiXStepWidth

                // X axis with marks
                axisPath.moveTo(stepsXDistance, measuredHeightWithGap - axisMarkLength / 2)
                axisPath.lineTo(stepsXDistance, measuredHeightWithGap + axisMarkLength)
            }

        }

        graphPath.reset()
        val expenseValues = data.expensesByDateMap.values.toList()


        if (expenseValues.isNotEmpty()) {
            val firstStepYGap = 32.dp()
            val stockHeightWithGap = measuredHeightWithGap - firstStepYGap


            val maxExpenseValue = expenseValues.maxOf { it }

            val axisYStepHeight = stockHeightWithGap / maxExpenseValue

            var axisYStockFinalHeight: Float
            var axisYStockFinalHeightChecked: Float


            var stepsXXDistance: Float = startXGap

            repeat(axisXDateMarkCount) { step ->
                if (step == 0) {
                    stepsXXDistance += firstStepXGap

                    axisYStockFinalHeight = stockHeightWithGap - expenseValues[step] * axisYStepHeight
                    axisYStockFinalHeightChecked = if (axisYStockFinalHeight < 1.dp()) axisYStockFinalHeight + firstStepYGap else axisYStockFinalHeight

                    graphPath.moveTo(stepsXXDistance, axisYStockFinalHeightChecked)


                } else {
                    stepsXXDistance += axiXStepWidth

                    axisYStockFinalHeight = stockHeightWithGap - expenseValues[step] * axisYStepHeight
                    axisYStockFinalHeightChecked = if (axisYStockFinalHeight < 1) axisYStockFinalHeight + firstStepYGap else axisYStockFinalHeight

                    graphPath.lineTo(stepsXXDistance, axisYStockFinalHeightChecked)

                }
            }
        }

        canvas.drawPath(axisPath, axisPaint)
        canvas.drawPath(graphPath, graphPaint)
    }


    fun setData(newData: CategoryDetailsGraphModel) {
        data = newData
        requestLayout()
        invalidate()
    }

    private fun Int.dp(): Float {
        return this * context.resources.displayMetrics.density
    }
}