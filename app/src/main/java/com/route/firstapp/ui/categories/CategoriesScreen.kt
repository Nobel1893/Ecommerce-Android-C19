package com.route.firstapp.ui.categories

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.route.firstapp.ui.AppRoutes
import com.route.firstapp.ui.ErrorWithRetry
import com.route.firstapp.ui.FullScreenLoading

// Entry composable only. It has no preview because it needs a Hilt ViewModel; the success state has
// its own file and previews (CategoriesSuccessState), loading and error use the shared
// FullScreenLoading and ErrorWithRetry.
@Composable
fun CategoriesScreen(modifier: Modifier = Modifier,
                     viewModel: CategoriesViewModel = hiltViewModel(),
                     onNavigate: (AppRoutes) -> Unit,
) {
    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        viewModel.doIntent(CategoriesContract.Intent.Init)
    }
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is CategoriesContract.Event.NavigateToProductsCatalog -> onNavigate(
                    AppRoutes.ProductsCatalog(
                        categoryId = event.categoryId,
                        subCategoryId = event.subCategoryId,
                    )
                )
            }
        }
    }
    val states = viewModel.states.collectAsStateWithLifecycle()

    when (val state = states.value) {
        CategoriesContract.State.Loading -> FullScreenLoading(
            modifier = modifier,
        )
        is CategoriesContract.State.Error -> ErrorWithRetry(
            modifier = modifier.padding(16.dp),
            onRetryClick = { viewModel.doIntent(CategoriesContract.Intent.Init) },
        )
        is CategoriesContract.State.Success -> CategoriesSuccessState(
            modifier = modifier,
            categories = state.categories,
            selectedCategory = state.selectedCategory,
            subCategoriesState = state.subCategoriesState,
            onCategoryClick = { category ->
                viewModel.doIntent(CategoriesContract.Intent.CategorySelected(category))
            },
            onSubCategoryClick = { subCategory ->
                viewModel.doIntent(CategoriesContract.Intent.SubCategoryClicked(subCategory))
            },
        )
    }
}
