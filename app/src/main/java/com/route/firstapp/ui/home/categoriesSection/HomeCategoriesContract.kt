package com.route.firstapp.ui.home.categoriesSection

import com.route.domain.data.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.ViewModel as AndroidViewModel


abstract class HomeCategoriesContract{

    abstract class ViewModel: AndroidViewModel(){
        abstract val states : StateFlow<CategoriesState>
        abstract val events : Flow<CategoriesEvent>
        abstract fun doIntent(intent: Intent)
    }

    // deep copy VS shallow copy
    sealed class CategoriesState{
        data class CategoriesSuccessState(
            val categories : List<Category?>
        ): CategoriesState()

        data class CategoriesErrorState(
            val message: String,
        ): CategoriesState()
        data class CategoriesLoadingState(
            val message: String,
        ): CategoriesState()

    }
    sealed class CategoriesEvent{
        data class ShowToast(val message: String): CategoriesEvent()
    }
    sealed class Intent{
        data object LoadCategories : Intent()
    }
}