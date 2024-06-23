package otus.homework.customview.presentation

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import otus.homework.customview.domain.Expense
import otus.homework.customview.domain.ExpenseCategoryService
import otus.homework.customview.domain.ExpenseRepository
import otus.homework.customview.util.ColorGenerator
import kotlin.math.round

interface PieChartRepository {
    suspend fun getPieChartModel(): PieChartModel
}

class PieChartRepositoryImpl(
    private val expenseRepository: ExpenseRepository,
    private val categoryService: ExpenseCategoryService,
    private val colorGenerator: ColorGenerator,
    private val dispatcherDefault: CoroutineDispatcher
) : PieChartRepository {

    override suspend fun getPieChartModel(): PieChartModel {
        val expensesList = expenseRepository.getExpenses()
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
                sweepAngle =
                    round(eachCategorySum * DEGREES_360 / allCategoriesSum)

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

    companion object {
        private const val DEGREES_360 = 360F
    }
}