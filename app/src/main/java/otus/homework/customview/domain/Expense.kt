package otus.homework.customview.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Expense(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("amount") val amount: Int,
    @SerialName("category") val category: String,
    @SerialName("time") val time: Long
): Parcelable
