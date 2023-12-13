package otus.homework.customview.util

import android.view.Choreographer
import java.util.concurrent.TimeUnit

class SimpleFpsCounterCallback : Choreographer.FrameCallback {

    private val choreographer = Choreographer.getInstance()

    private var currentFrameTime = 0L
    private var renderedFramesCount = 0

    private var fpsCounterListener: FpsCounterListener? = null

    override fun doFrame(frameTimeNanos: Long) {
        val frameTimeMillis = TimeUnit.NANOSECONDS.toMillis(frameTimeNanos)

        if (currentFrameTime > 0) {

            renderedFramesCount++

            val timeInterval = frameTimeMillis - currentFrameTime
            if (timeInterval > CONTROL_TIME_GAP_MILLIS) {

                val fpsDouble = renderedFramesCount / timeInterval.toDouble() * 1000
                val fps = fpsDouble.toInt()

                fpsCounterListener?.showFps(fps)

                currentFrameTime = frameTimeMillis
                renderedFramesCount = 0
            }

        } else {
            currentFrameTime = frameTimeMillis
        }
        choreographer.postFrameCallback(this)
    }

    fun setListener(listener: FpsCounterListener) {
        fpsCounterListener = listener
    }

    fun startCounter() {
        choreographer.postFrameCallback(this)
    }
    fun stopCounter() {
        choreographer.removeFrameCallback(this)
    }
    companion object {
        private const val CONTROL_TIME_GAP_MILLIS = 700
    }
}

interface FpsCounterListener {
    fun showFps(fps: Int)
}