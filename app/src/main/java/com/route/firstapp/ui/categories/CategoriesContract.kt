package com.route.firstapp.ui.categories

import com.route.domain.data.Category
import com.route.domain.data.SubCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface CategoriesContract {
    abstract class ViewModel : androidx.lifecycle.ViewModel(){
        abstract val states : StateFlow<State>
        abstract val events : Flow<Event>
        abstract fun doIntent(intent: Intent)
    }

    sealed class State{
        data class Success(
            val categories: List<Category>,
            val selectedCategory: Category?,
            val subCategoriesState: SubCategoriesState,
        ): State()
        data class Error(
            val message: String,
        ): State()
        data object Loading: State()
    }

    // second layer of the tree, reloaded every time a category is selected
    sealed class SubCategoriesState{
        data class Success(
            val subCategories: List<SubCategory>
        ): SubCategoriesState()
        data class Error(
            val message: String,
        ): SubCategoriesState()
        data object Loading: SubCategoriesState()
    }

    sealed class Intent{
        data object Init: Intent()
        data class CategorySelected(val category: Category): Intent()
        data class SubCategoryClicked(val subCategory: SubCategory): Intent()
    }
    sealed class Event{
        data class NavigateToProductsCatalog(
            val categoryId: String,
            val subCategoryId: String,
        ): Event()
    }
}
