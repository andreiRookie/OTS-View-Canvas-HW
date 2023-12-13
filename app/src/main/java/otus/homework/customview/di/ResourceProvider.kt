package otus.homework.customview.di

import android.content.Context
import androidx.annotation.RawRes
import java.io.InputStream

interface ResourceProvider {
    fun getRawResource(@RawRes resId: Int): InputStream
}

class ResourceProviderImpl(
    private val context: Context
) : ResourceProvider {
    override fun getRawResource(@RawRes resId: Int): InputStream {
        return context.resources.openRawResource(resId)
    }
}