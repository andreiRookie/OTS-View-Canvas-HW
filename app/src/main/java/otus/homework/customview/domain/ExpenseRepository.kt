package otus.homework.customview.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import otus.homework.customview.R
import otus.homework.customview.di.ResourceProvider
import otus.homework.customview.presentation.ColorGenerator
import otus.homework.customview.presentation.PieChartModel
import otus.homework.customview.presentation.SectorModel
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.math.round


interface ExpenseRepository {
    suspend fun getPieChartModel(): PieChartModel
}

class ExpenseRepositoryImpl(
    private val resProvider: ResourceProvider,
    private val categoryService: ExpenseCategoryService,
    private val colorGenerator: ColorGenerator,
    private val dispatcherIo: CoroutineDispatcher,
    private val dispatcherDefault: CoroutineDispatcher
): ExpenseRepository {

    override suspend fun getPieChartModel(): PieChartModel {

        val expensesList = getExpenseList()
        val sectorList = getSortedSectorList(expensesList)

        return PieChartModel(sectors = sectorList)
    }

    private suspend fun getSortedSectorList(expensesList: List<Expense>): List<SectorModel> {
        val resultSectorList = mutableListOf<SectorModel>()

        withContext(dispatcherDefault) {

            val categoriesMap = categoryService.groupByCategoryIntoMap(expensesList)
            val allCategoriesSum = categoryService.getAllExpensesAmount(expensesList)

            val sectorList: MutableList<SectorModel> = mutableListOf()
            var sweepAngle = 0f
            var eachCategorySum = 0

            categoriesMap.forEach { entry ->
                entry.value.forEach { expense ->
                    eachCategorySum += expense.amount
                }
                sweepAngle = round(eachCategorySum * DEGREES_360 / allCategoriesSum)

                sectorList += SectorModel(
                    categoryName = entry.key,
                    sweepAngle = sweepAngle,
                    color = colorGenerator.generateColor(),
                    totalValue = eachCategorySum,
                    expenseList = entry.value
                )
                eachCategorySum = 0
            }

            val sortedSectorList = sectorList.sortedByDescending { it.sweepAngle }

            var startAngle = 0f

            sortedSectorList.forEach { model ->
                resultSectorList += model.copy(startAngle = startAngle)
                startAngle += model.sweepAngle
            }
        }
        return resultSectorList
    }

    private suspend fun getExpenseList(): List<Expense> {

        var list: List<Expense>

        withContext(dispatcherIo) {
            val payload = resProvider.getRawResource(R.raw.payload_10_categories)

            BufferedReader(InputStreamReader(payload)).use { reader ->
                list = Json.decodeFromString<List<Expense>>(reader.readText())
            }
        }
        return list
    }

    companion object {
        private const val DEGREES_360 = 360F
    }
}