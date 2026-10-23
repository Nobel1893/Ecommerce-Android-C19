package com.route.firstapp.ui.categories

import androidx.lifecycle.viewModelScope
import com.route.domain.data.Category
import com.route.domain.data.Resource
import com.route.domain.data.SubCategory
import com.route.domain.usecase.GetCategoriesUseCase
import com.route.domain.usecase.GetSubCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getSubCategoriesUseCase: GetSubCategoriesUseCase,
) : CategoriesContract.ViewModel(){

    private val _events = Channel<CategoriesContract.Event>(Channel.BUFFERED)
    override val events: Flow<CategoriesContract.Event> = _events.receiveAsFlow()

    private val _states = MutableStateFlow<CategoriesContract.State>(CategoriesContract.State.Loading)
    override val states: StateFlow<CategoriesContract.State> = _states.asStateFlow()

    private var subCategoriesJob: Job? = null

    override fun doIntent(intent: CategoriesContract.Intent) {
        when (intent) {
            CategoriesContract.Intent.Init -> {
                // Init is sent again when the tab comes back into composition, keep the user's selection
                if (_states.value !is CategoriesContract.State.Success) loadCategories()
            }
            is CategoriesContract.Intent.CategorySelected -> selectCategory(category = intent.category)
            is CategoriesContract.Intent.SubCategoryClicked -> handleSubCategoryClick(subCategory = intent.subCategory)
        }
    }

    private fun loadCategories(){
        viewModelScope.launch {
            getCategoriesUseCase.getCategories()
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _states.emit(CategoriesContract.State.Loading)
                        }
                        is Resource.Error -> {
                            _states.emit(
                                CategoriesContract.State.Error(message = resource.throwable.message ?: "")
                            )
                        }
                        is Resource.Success -> {
                            val categories = resource.data?.filterNotNull() ?: listOf()
                            _states.emit(
                                CategoriesContract.State.Success(
                                    categories = categories,
                                    selectedCategory = null,
                                    subCategoriesState = CategoriesContract.SubCategoriesState.Success(listOf()),
                                )
                            )
                            categories.firstOrNull()?.let { firstCategory ->
                                selectCategory(category = firstCategory)
                            }
                        }
                    }
                }
        }
    }

    private fun selectCategory(category: Category){
        val state = _states.value as? CategoriesContract.State.Success ?: return
        // re-selecting the current category only reloads after a failure (retry)
        if (state.selectedCategory?.id == category.id &&
            state.subCategoriesState !is CategoriesContract.SubCategoriesState.Error) return

        _states.value = state.copy(selectedCategory = category)
        // drop the previous category's request so its result can't overwrite this one
        subCategoriesJob?.cancel()
        subCategoriesJob = viewModelScope.launch {
            getSubCategoriesUseCase.getSubCategories(categoryId = category.id)
                .collect { resource ->
                    val subCategoriesState = when (resource) {
                        is Resource.Loading -> CategoriesContract.SubCategoriesState.Loading
                        is Resource.Error -> CategoriesContract.SubCategoriesState.Error(
                            message = resource.throwable.message ?: ""
                        )
                        is Resource.Success -> CategoriesContract.SubCategoriesState.Success(
                            subCategories = resource.data ?: listOf()
                        )
                    }
                    _states.update { current ->
                        if (current is CategoriesContract.State.Success)
                            current.copy(subCategoriesState = subCategoriesState)
                        else current
                    }
                }
        }
    }

    private fun handleSubCategoryClick(subCategory: SubCategory){
        viewModelScope.launch {
            _events.send(
                CategoriesContract.Event.NavigateToProductsCatalog(
                    categoryId = subCategory.categoryId,
                    subCategoryId = subCategory.id,
                )
            )
        }
    }
}
