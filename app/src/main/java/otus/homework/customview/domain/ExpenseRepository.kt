package otus.homework.customview.domain

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import otus.homework.customview.R
import otus.homework.customview.di.ResourceProvider
import otus.homework.customview.presentation.CategoriesDataModel
import otus.homework.customview.presentation.CategoryModel
import otus.homework.customview.presentation.ColorGenerator
import otus.homework.customview.presentation.PieChartModel
import otus.homework.customview.presentation.SectorModel
import otus.homework.customview.util.TAG
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.math.round


interface ExpenseRepository {
    fun getPieChartModel(): PieChartModel
    fun getExpensesDataModel(): CategoriesDataModel
}

class ExpenseRepositoryImpl(
    private val resProvider: ResourceProvider,
    private val categoryService: ExpenseCategoryService,
    private val colorGenerator: ColorGenerator
): ExpenseRepository {

    override fun getPieChartModel(): PieChartModel {

        val expensesList = getExpenseList()

        val categoriesMap = categoryService.groupByCategoryIntoMap(expensesList)
        val allCategoriesSum = categoryService.getAllExpensesAmount(expensesList)

        val result: MutableList<SectorModel> = mutableListOf()

        var startAngle = 0f
        var sweepAngle = 0f
        var eachCategorySum = 0

        categoriesMap.forEach { entry ->
            entry.value.forEach { expense ->
                eachCategorySum += expense.amount
            }

            sweepAngle = round(eachCategorySum * DEGREES_360 / allCategoriesSum)


            result += SectorModel(
                name = entry.key,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                color = colorGenerator.generateColor()
                )

            startAngle += sweepAngle
            eachCategorySum = 0
        }
        return PieChartModel(sectors = result)
    }

    override fun getExpensesDataModel(): CategoriesDataModel {
        val expensesList = getExpenseList()

        val categoriesMap = categoryService.groupByCategoryIntoMap(expensesList)

        val result: MutableList<CategoryModel> = mutableListOf()

        var eachCategorySum = 0

        categoriesMap.forEach { entry ->
            entry.value.forEach { expense ->
                eachCategorySum += expense.amount
            }

            result += CategoryModel(
                name = entry.key,
                totalValue = eachCategorySum,
                expenseList = entry.value
            )

            eachCategorySum = 0
        }
        return CategoriesDataModel(categories = result)
    }

    private fun getExpenseList(): List<Expense> {
        val payload = resProvider.getRawResource(R.raw.payload_10_categories)

        var list: List<Expense>

        BufferedReader(InputStreamReader(payload)).use { reader ->
            list = Json.decodeFromString<List<Expense>>(reader.readText())
        }
        println("${this.TAG} ExpenseList $list")
        return list
    }

    companion object {
        private const val DEGREES_360 = 360F
    }
}