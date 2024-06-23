package otus.homework.customview.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import otus.homework.customview.util.TAG

class PieChartViewModel(
    private val repository: PieChartRepository
) : ViewModel() {

    private val _state = MutableLiveData<PieChartModel>()
    val state: LiveData<PieChartModel> get() = _state

    init {
        getChartData()
    }

    private fun getChartData() {
        viewModelScope.launch {
            _state.postValue(repository.getPieChartModel())
        }
    }

    companion object {

        fun Factory(repository: PieChartRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return when (modelClass) {
                        PieChartViewModel::class.java -> {
                            PieChartViewModel(repository) as T
                        }
                        else -> {
                            error("$TAG unknown $modelClass")
                        }
                    }
                }
            }
        }
    }
}