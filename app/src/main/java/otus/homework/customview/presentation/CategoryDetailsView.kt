package otus.homework.customview.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
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
        strokeWidth = 16f
        flags = Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG
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
        val pointerArmOffset = 4.dp()

        // start
        axisPath.reset()
        axisPath.moveTo(startXGap,0f)

        // Y axis pointer
        axisPath.lineTo(pointerArmOffset, startYGap)
        axisPath.moveTo(startXGap,0f)
        axisPath.lineTo(startXGap + pointerArmOffset, startYGap)

        // axis
        axisPath.moveTo(startXGap,0f)
        axisPath.lineTo(startXGap, height - startYGap)
        axisPath.lineTo(width.toFloat(), height - startYGap)

        // X axis pointer
        axisPath.lineTo(width - startXGap,height - startYGap - pointerArmOffset)
        axisPath.moveTo(width.toFloat(),height - startYGap)
        axisPath.lineTo(width - startXGap, height - startYGap + pointerArmOffset)

        // X axis day steps
        val stepXGap = 32.dp()
        val axisMarkLength = 16.dp()
        val axisMarkCount = data.expensesByDateMap.keys.size
        val stepLength = (width - startXGap - stepXGap * 2) / axisMarkCount

        var stepsXDistance: Float = startXGap
        repeat(axisMarkCount) { step ->
            if (step == 0) {
                stepsXDistance += stepXGap
                axisPath.moveTo(stepsXDistance, height - startYGap - axisMarkLength / 2)
                axisPath.lineTo(stepsXDistance, height - startYGap + axisMarkLength)
            } else {
                stepsXDistance += stepLength
                axisPath.moveTo(stepsXDistance, height - startYGap - axisMarkLength / 2)
                axisPath.lineTo(stepsXDistance, height - startYGap + axisMarkLength)
            }

        }

        canvas.drawPath(axisPath, axisPaint)

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