package otus.homework.customview.presentation

import android.os.Parcelable
import android.view.View.BaseSavedState
import kotlinx.parcelize.Parcelize
import otus.homework.customview.domain.Expense

@Parcelize
data class SectorModel(
    val name: String,
    val sweepAngle: Float,
    val color: Int
): Parcelable

@Parcelize
data class PieChartModel(
    val sectors: List<SectorModel>
): Parcelable

data class CategoryModel(
    val name: String,
    val totalValue: Int,
    val expenseList: List<Expense>
)

data class CategoriesDataModel(
    val categories: List<CategoryModel>
)

//@Parcelize
//data class ChartState(
//    val superSavedState: Parcelable?,
//    val pieChartModel: PieChartModel
//) : Parcelable, BaseSavedState(superSavedState)