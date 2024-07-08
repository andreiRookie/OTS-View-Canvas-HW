package otus.homework.customview.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import otus.homework.customview.util.TAG

class CategoryDetailsViewModel(
    private val repository: CategoryDetailsRepository
) : ViewModel() {

    private var _state = MutableLiveData<CategoryDetailsState>()

    val state: LiveData<CategoryDetailsState> get() = _state

    fun updateStateWithCategory(category: String, color: Int) {
        viewModelScope.launch {
            _state.value = CategoryDetailsState(
                category,
                color,
                repository.getCategoryDetailsGraphModel(category)
            )
        }

    }

    companion object {

        fun Factory(repository: CategoryDetailsRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return when (modelClass) {
                        CategoryDetailsViewModel::class.java -> {
                            CategoryDetailsViewModel(repository) as T
                        } else -> {
                            error("$TAG unknown $modelClass")
                        }
                    }
                }
            }
        }
    }
}