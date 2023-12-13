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
            255,
            random.nextInt(256),
            random.nextInt(256),
            random.nextInt(256)
        )
    }
}