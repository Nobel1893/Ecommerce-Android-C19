package com.route.firstapp.ui.home.categoriesSection

import androidx.lifecycle.viewModelScope
import com.route.domain.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
        _events.trySend(
            HomeCategoriesContract.CategoriesEvent.ShowToast("Started Loading")
        )
        viewModelScope.launch {
            try {
                val categories = getCategoriesUseCase.getCategories()
                _states.value = HomeCategoriesContract.CategoriesState.CategoriesSuccessState(
                    categories
                )
                _events.trySend(
                    HomeCategoriesContract.CategoriesEvent.ShowToast("Categories Emitted")
                )

            }catch (e: Throwable){
                _events.trySend(
                    HomeCategoriesContract.CategoriesEvent.ShowToast("Categories Error")
                )

                _states.value = HomeCategoriesContract.CategoriesState.CategoriesErrorState(
                    "Something Went Wrong"
                )
            }
        }
    }
}