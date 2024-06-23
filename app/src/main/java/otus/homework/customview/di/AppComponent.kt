package otus.homework.customview.di

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import otus.homework.customview.domain.ExpenseCategoryService
import otus.homework.customview.domain.ExpenseCategoryServiceImpl
import otus.homework.customview.domain.ExpenseRepository
import otus.homework.customview.domain.ExpenseRepositoryImpl
import otus.homework.customview.util.ColorGenerator
import otus.homework.customview.util.ColorGeneratorImpl
import otus.homework.customview.presentation.PieChartViewModel

class AppComponent private constructor(context: Context) {

    private val resProvider: ResourceProvider by lazy { ResourceProviderImpl(context) }

    private val categoryService: ExpenseCategoryService by lazy { ExpenseCategoryServiceImpl() }

    private val colorGenerator: ColorGenerator by lazy { ColorGeneratorImpl() }

    private val dispatcherIo: CoroutineDispatcher by lazy { Dispatchers.IO }

    private val dispatcherDefault: CoroutineDispatcher by lazy { Dispatchers.Default }

    private val repository: ExpenseRepository by lazy {
        ExpenseRepositoryImpl(
            resProvider,
            categoryService,
            colorGenerator,
            dispatcherIo,
            dispatcherDefault)
    }

    val viewModel: PieChartViewModel by lazy {
        PieChartViewModel.Factory(repository).create(PieChartViewModel::class.java)
    }

    companion object {
        lateinit var INSTANCE: AppComponent

        fun init(context: Context) {
            INSTANCE = AppComponent(context)
        }
    }
}