package com.route.firstapp.ui.home.categoriesSection

import androidx.lifecycle.viewModelScope
import com.route.domain.data.Resource
import com.route.domain.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class HomeCategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
) :HomeCategoriesContract.ViewModel(){

    private var _state = HomeCategoriesContract.CategoriesState.CategoriesLoadingState(
        "Loading"
    )
    private val _states = MutableStateFlow<HomeCategoriesContract.CategoriesState>(_state)
    // state flow - SharedStateFlow - Channel
    override val states: StateFlow<HomeCategoriesContract.CategoriesState> = _states.asStateFlow()


    val _events : Channel<HomeCategoriesContract.CategoriesEvent> = Channel(Channel.BUFFERED)
    // init block calls when object is Created
    override val events = _events.receiveAsFlow()
    override fun doIntent(intent: HomeCategoriesContract.Intent) {
        when(intent){
            HomeCategoriesContract.Intent.LoadCategories -> {
                getCategories()
            }
        }
    }

    init {
        getCategories()
    }
    private fun getCategories(){
        viewModelScope.launch {
            getCategoriesUseCase.getCategories()
                .collect { resource ->
                    when (resource) {
                        is Resource.Error ->{
                            _events.trySend(
                                HomeCategoriesContract.CategoriesEvent.ShowToast("Categories Error")
                            )

                            _states.value = HomeCategoriesContract.CategoriesState.CategoriesErrorState(
                                "Something Went Wrong"
                            )
                        }
                        is Resource.Loading -> {
                            _events.trySend(
                                HomeCategoriesContract.CategoriesEvent.ShowToast("Started Loading")
                            )
                        }
                        is Resource.Success -> {
                            _states.value = HomeCategoriesContract.CategoriesState.CategoriesSuccessState(
                                resource.data ?: listOf()
                            )
                            _events.trySend(
                                HomeCategoriesContract.CategoriesEvent.ShowToast("Categories Emitted")
                            )
                        }
                    }
                }
        }


    }
}