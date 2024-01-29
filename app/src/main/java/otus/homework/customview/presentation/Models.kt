package otus.homework.customview.presentation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import otus.homework.customview.domain.Expense

@Parcelize
data class SectorModel(
    val categoryName: String,
    val startAngle: Float = 0f,
    val sweepAngle: Float,
    val color: Int,
    val totalValue: Int,
    val expenseList: List<Expense>
): Parcelable

@Parcelize
data class PieChartModel(
    val sectors: List<SectorModel>
): Parcelable

data class CategoryData(
    val categoryName: String,
    val totalValue: Int,
    val expenseList: List<Expense>
)


//@Parcelize
//data class ChartState(
//    val superSavedState: Parcelable?,
//    val pieChartModel: PieChartModel
//) : Parcelable, BaseSavedState(superSavedState)