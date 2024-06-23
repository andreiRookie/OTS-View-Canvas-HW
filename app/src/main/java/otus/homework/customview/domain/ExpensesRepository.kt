package otus.homework.customview.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import otus.homework.customview.R
import otus.homework.customview.di.ResourceProvider
import java.io.BufferedReader
import java.io.InputStreamReader


interface ExpenseRepository {
    suspend fun getExpenses(): List<Expense>
}

class ExpenseRepositoryImpl(
    private val resProvider: ResourceProvider,
    private val dispatcherIo: CoroutineDispatcher,
    private val cache: ExpensesCache
) : ExpenseRepository {

    override suspend fun getExpenses(): List<Expense> {
        return cache.getExpenses().ifEmpty {

            withContext(dispatcherIo) {
                val payload = resProvider.getRawResource(R.raw.payload_10_categories)

                BufferedReader(InputStreamReader(payload)).use { reader ->
                    Json.decodeFromString<List<Expense>>(reader.readText())
                }
            }
        }.also { cache.saveExpenses(it) }
    }
}