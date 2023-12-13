package otus.homework.customview.presentation

import otus.homework.customview.domain.Expense

data class CategoryChartModel(
    val name: String,
    val sectorSweepAngle: Float,
    val totalExpenseSum: Int,
    val expenseList: List<Expense>
)

data class PieChartModel(
    val categories: List<CategoryChartModel>
)