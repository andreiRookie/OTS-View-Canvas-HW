package otus.homework.customview.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Expense(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("amount") val amount: Int,
    @SerialName("category") val category: String,
    @SerialName("time") val time: Long
)
