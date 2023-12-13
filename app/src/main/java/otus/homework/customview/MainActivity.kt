package otus.homework.customview

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import otus.homework.customview.util.FpsCounterListener
import otus.homework.customview.util.SimpleFpsCounterCallback

class MainActivity : AppCompatActivity() {

    private lateinit var fpsCounterView: TextView
    private lateinit var fpsCounter: SimpleFpsCounterCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fpsCounterView = this.findViewById(R.id.fps_counter_text_view)

        fpsCounter = SimpleFpsCounterCallback()
        fpsCounter.setListener(object : FpsCounterListener {
            override fun showFps(fps: Int) {
                fpsCounterView.text = getString(R.string.fps_counter_template, fps.toString())
            }
        })
    }

    override fun onStart() {
        super.onStart()
        fpsCounter.startCounter()
    }
    override fun onStop() {
        super.onStop()
        fpsCounter.stopCounter()
    }
}