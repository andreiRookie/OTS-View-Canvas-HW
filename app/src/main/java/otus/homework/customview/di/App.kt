package otus.homework.customview.di

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppComponent.init(applicationContext)
    }
}