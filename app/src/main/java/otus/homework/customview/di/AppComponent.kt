package otus.homework.customview.di

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import otus.homework.customview.domain.ExpenseCategoryService
import otus.homework.customview.domain.ExpenseCategoryServiceImpl
import otus.homework.customview.domain.ExpenseRepository
import otus.homework.customview.domain.ExpenseRepositoryImpl
import otus.homework.customview.domain.ExpensesCache
import otus.homework.customview.domain.ExpensesCacheImplObject
import otus.homework.customview.presentation.PieChartRepository
import otus.homework.customview.presentation.PieChartRepositoryImpl
import otus.homework.customview.presentation.PieChartViewModel
import otus.homework.customview.util.ColorGenerator
import otus.homework.customview.util.ColorGeneratorImpl

class AppComponent private constructor(context: Context) {

    private val resProvider: ResourceProvider by lazy { ResourceProviderImpl(context) }

    private val categoryService: ExpenseCategoryService by lazy { ExpenseCategoryServiceImpl() }

    private val colorGenerator: ColorGenerator by lazy { ColorGeneratorImpl() }

    private val dispatcherIo: CoroutineDispatcher by lazy { Dispatchers.IO }

    private val dispatcherDefault: CoroutineDispatcher by lazy { Dispatchers.Default }

    private val cache: ExpensesCache by lazy { ExpensesCacheImplObject }

    private val expenseRepository: ExpenseRepository by lazy {
        ExpenseRepositoryImpl(
            resProvider,
            dispatcherIo,
            cache
        )
    }

    private val pieChartRepository: PieChartRepository by lazy {
        PieChartRepositoryImpl(
            expenseRepository,
            categoryService,
            colorGenerator,
            dispatcherDefault
        )
    }

    val viewModel: PieChartViewModel by lazy {
        PieChartViewModel.Factory(pieChartRepository).create(PieChartViewModel::class.java)
    }

    companion object {
        lateinit var INSTANCE: AppComponent

        fun init(context: Context) {
            INSTANCE = AppComponent(context)
        }
    }
}