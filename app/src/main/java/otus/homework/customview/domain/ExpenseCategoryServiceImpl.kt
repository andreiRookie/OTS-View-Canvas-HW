package otus.homework.customview.domain


interface ExpenseCategoryService {
    fun groupByCategoryIntoMap(list: List<Expense>): Map<String, List<Expense>>
    fun getAllExpensesAmount(list: List<Expense>): Int
}

class ExpenseCategoryServiceImpl : ExpenseCategoryService {

    override fun groupByCategoryIntoMap(list: List<Expense>): Map<String, List<Expense>> {

        val categories: MutableMap<String, MutableList<Expense>> = mutableMapOf()

        list.forEach { expense ->

            if (!categories.containsKey(expense.category)) {
                categories[expense.category] = mutableListOf(expense)
            } else {
                categories[expense.category]?.add(expense)
            }
        }
        return categories
    }

    override fun getAllExpensesAmount(list: List<Expense>): Int {
        var allExpensesAmount = 0

        list.forEach { expense -> allExpensesAmount += expense.amount }

        return allExpensesAmount
    }
}

//fun <K, V> MutableMap<K, V>.putOrCreate(newKey: K, newValue: V, valueWrapper: () -> V) {
//    if (!this.containsKey(newKey)) {
//        this[newKey] = valueWrapper()
//    } else {
//        this[newKey] = newValue
//    }
//}