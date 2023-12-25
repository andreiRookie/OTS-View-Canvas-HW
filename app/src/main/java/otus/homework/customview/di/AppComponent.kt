package otus.homework.customview.di

import android.content.Context
import otus.homework.customview.domain.ExpenseCategoryService
import otus.homework.customview.domain.ExpenseCategoryServiceImpl
import otus.homework.customview.domain.ExpenseRepository
import otus.homework.customview.domain.ExpenseRepositoryImpl
import otus.homework.customview.presentation.ColorGenerator
import otus.homework.customview.presentation.ColorGeneratorImpl
import otus.homework.customview.presentation.PieChartViewModel

class AppComponent private constructor(context: Context) {

    val resProvider: ResourceProvider by lazy { ResourceProviderImpl(context) }

    val categoryService: ExpenseCategoryService by lazy { ExpenseCategoryServiceImpl() }

    val colorGenerator: ColorGenerator by lazy { ColorGeneratorImpl() }

    val repository: ExpenseRepository by lazy {
        ExpenseRepositoryImpl(resProvider, categoryService, colorGenerator)
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