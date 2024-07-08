package otus.homework.customview.presentation

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import otus.homework.customview.domain.ExpenseCategoryService
import otus.homework.customview.domain.ExpenseRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface CategoryDetailsRepository {
    suspend fun getCategoryDetailsGraphModel(category: String): CategoryDetailsGraphModel
}

class CategoryDetailsRepositoryImpl(
    private val expenseRepository: ExpenseRepository,
    private val categoryService: ExpenseCategoryService,
    private val dispatcherDefault: CoroutineDispatcher
) : CategoryDetailsRepository {

    private val simpleDateFormat = SimpleDateFormat("dd-MM-yyy", Locale.getDefault())

    override suspend fun getCategoryDetailsGraphModel(category: String): CategoryDetailsGraphModel {
        val expenses = expenseRepository.getExpenses()

        val deferredResult = withContext(dispatcherDefault) {

            async {
                val categoriesMap = categoryService.groupByCategoryIntoMap(expenses)

                var date = ""
                var newSum = 0
                val expensesByDatesMap: MutableMap<String, Int> = mutableMapOf()

                val expensesSortedByDate = categoriesMap[category]?.sortedBy { it.time }

                expensesSortedByDate?.forEach { expense ->
                    date = expense.time.formatDate()

                    if (expensesByDatesMap.containsKey(date)) {
                        val currSum = expensesByDatesMap[date]
                        newSum = currSum?.plus(expense.amount)!!
                        expensesByDatesMap[date] = newSum

                    } else {
                        expensesByDatesMap[date] = expense.amount
                    }
                }

                CategoryDetailsGraphModel(
                    expensesByDateMap = expensesByDatesMap
                )
            }
        }

        return deferredResult.await()
    }

    private fun Long.formatDate(): String {
        return simpleDateFormat.format(Date(this))
    }
}