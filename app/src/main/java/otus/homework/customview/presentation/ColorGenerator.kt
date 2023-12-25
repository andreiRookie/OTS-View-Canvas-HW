package otus.homework.customview.presentation

import android.graphics.Color
import androidx.annotation.ColorInt
import java.util.Random

interface ColorGenerator {

    @ColorInt
    fun generateColor(): Int
}

class ColorGeneratorImpl : ColorGenerator {
    override fun generateColor(): Int {
        val random = Random()
        return Color.argb(
            BOUND,
            random.nextInt(BOUND),
            random.nextInt(BOUND),
            random.nextInt(BOUND)
        )
    }

    companion object {
        private const val BOUND = 255
    }
}