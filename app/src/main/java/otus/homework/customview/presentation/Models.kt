package otus.homework.customview.presentation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import otus.homework.customview.domain.Expense

@Parcelize
data class SectorModel(
    val categoryName: String,
    val sweepAngle: Float,
    val color: Int,
    val totalValue: Int,
    val expenseList: List<Expense>
): Parcelable

@Parcelize
data class PieChartModel(
    val sectors: List<SectorModel>
): Parcelable


//@Parcelize
//data class ChartState(
//    val superSavedState: Parcelable?,
//    val pieChartModel: PieChartModel
//) : Parcelable, BaseSavedState(superSavedState)