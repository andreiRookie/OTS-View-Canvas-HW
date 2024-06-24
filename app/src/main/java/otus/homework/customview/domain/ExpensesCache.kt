package otus.homework.customview.domain


interface ExpensesCache {
    suspend fun getExpenses():  List<Expense>
    suspend fun saveExpenses(list: List<Expense>)
}

class ExpensesCacheImpl : ExpensesCache {

    private var cache: List<Expense> = mutableListOf()

    override suspend fun getExpenses():  List<Expense> {
        return cache
    }

    override suspend fun saveExpenses(list: List<Expense>) {
        cache = list
    }
}