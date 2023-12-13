package otus.homework.customview.domain

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import otus.homework.customview.R
import otus.homework.customview.di.ResourceProvider
import otus.homework.customview.presentation.PieChartModel
import otus.homework.customview.presentation.CategoryChartModel
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.math.round


interface ExpenseRepository {
    fun getPieChartModel(): PieChartModel
}

class ExpenseRepositoryImpl(
    private val resProvider: ResourceProvider,
    private val categoryService: ExpenseCategoryService
): ExpenseRepository {


    override fun getPieChartModel(): PieChartModel {

        val expensesList = getExpenseList()

        val categoriesMap = categoryService.groupByCategoryIntoMap(expensesList)
        val allExpensesSum = categoryService.getAllExpensesAmount(expensesList)

        val result: MutableList<CategoryChartModel> = mutableListOf()

        var sweepAngle = 0f
        var eachCategorySum = 0

        categoriesMap.forEach { entry ->
            entry.value.forEach { expense ->
                eachCategorySum += expense.amount
            }

            sweepAngle = round(eachCategorySum * DEGREES_360 / allExpensesSum)

            result += CategoryChartModel(
                name = entry.key,
                sectorSweepAngle = sweepAngle,
                totalExpenseSum = eachCategorySum,
                expenseList = entry.value
                )

            eachCategorySum = 0
        }
        return PieChartModel(categories = result)
    }



    private fun getExpenseList(): List<Expense> {
        val payload = resProvider.getRawResource(R.raw.payload)

        var list: List<Expense>

        BufferedReader(InputStreamReader(payload)).use { reader ->
            list = Json.decodeFromString<List<Expense>>(reader.readText())
        }
        return list
    }

    companion object {
        private const val DEGREES_360 = 360F
    }
}