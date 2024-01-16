package otus.homework.customview.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import otus.homework.customview.R
import otus.homework.customview.di.AppComponent

class PieChartFragment : Fragment(R.layout.fragment_pie_chart_layout) {

    private lateinit var viewModel: PieChartViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = AppComponent.INSTANCE.viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pieChartView = view.findViewById<PieChartView>(R.id.pie_chart_view)

//        val setChartButton = view.findViewById<Button>(R.id.set_chart_button)
//        setChartButton.setOnClickListener {
//            pieChartView.setPieChartModel(stubPieChartModel)
//        }

        viewModel.state.observe(viewLifecycleOwner) {
            pieChartView.setPieChartModel(it)
        }
    }
}