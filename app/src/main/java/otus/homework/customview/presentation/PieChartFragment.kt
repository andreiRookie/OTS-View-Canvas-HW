package otus.homework.customview.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import otus.homework.customview.R
import otus.homework.customview.di.AppComponent
import otus.homework.customview.di.ResourceProvider
import otus.homework.customview.domain.Expense
import java.io.BufferedReader
import java.io.InputStreamReader

class PieChartFragment : Fragment(R.layout.fragment_pie_chart_layout) {

    private lateinit var resourceProvider: ResourceProvider
    private lateinit var viewModel: PieChartViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        resourceProvider = AppComponent.INSTANCE.getResourceProvider(context)
        resourceProvider = AppComponent.INSTANCE.resProvider
        viewModel = AppComponent.INSTANCE.viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pieChartView = view.findViewById<PieChartView>(R.id.pie_chart_view)

        viewModel.state.observe(viewLifecycleOwner) {
            pieChartView.setPieChartModel(it)
        }
//        val expenseList = getExpenseList()
//        println("${this.TAG} expenseList $expenseList")
//
//        val categoriesMap = ExpenseCategoryServiceImpl().groupByCategoryIntoMap(expenseList)
//        println("${this.TAG} categoriesMap $categoriesMap")
//        pieChartView.setExpenseCategories(categoriesMap)
    }

    private fun getExpenseList(): List<Expense> {
        val payload = resourceProvider.getRawResource(R.raw.payload)

        var list: List<Expense>

        BufferedReader(InputStreamReader(payload)).use { reader ->
            list = Json.decodeFromString<List<Expense>>(reader.readText())
        }
        return list
    }
}