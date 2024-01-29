package otus.homework.customview.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
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

        val onSectorClickListener = object : OnSectorClickListener {
            override fun onSectorClick(categoryData: CategoryData) {
                Toast.makeText(context,
                    "${categoryData.categoryName} - ${categoryData.totalValue}rub.",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }
        pieChartView.setSectorClickListener(onSectorClickListener)

        viewModel.state.observe(viewLifecycleOwner) {
            pieChartView.setPieChartModel(it)
        }
    }
}