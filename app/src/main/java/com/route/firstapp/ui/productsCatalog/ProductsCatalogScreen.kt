package com.route.firstapp.ui.productsCatalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
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

// Entry composable only. It has no preview because it needs a Hilt ViewModel; the header and the
// success state have their own file and previews (ProductsCatalogHeader, ProductsCatalogSuccessState),
// loading and error use the shared FullScreenLoading and ErrorWithRetry.
@Composable
fun ProductsCatalogScreen(modifier: Modifier = Modifier,
                          categoryId: String,
                          subCategoryId: String,
                          viewModel: ProductsCatalogViewModel = hiltViewModel(),
                          onNavigate: (AppRoutes) -> Unit,
) {
    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        viewModel.doIntent(
            ProductsCatalogContract.Intent.Init(
                categoryId = categoryId,
                subCategoryId = subCategoryId,
            )
        )
    }
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is ProductsCatalogContract.Event.NavigateToProductDetails -> onNavigate(
                    AppRoutes.ProductDetails(productId = event.productId)
                )
            }
        }
    }
    val states = viewModel.states.collectAsStateWithLifecycle()

    // a full-screen destination, so it keeps clear of the system bars and camera cutout itself
    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding(),
    ) {
        ProductsCatalogHeader(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
        )
        when (val state = states.value) {
            ProductsCatalogContract.State.Loading -> FullScreenLoading(
                modifier = Modifier.weight(1f),
            )
            is ProductsCatalogContract.State.Error -> ErrorWithRetry(
                modifier = Modifier.padding(16.dp),
                onRetryClick = {
                    viewModel.doIntent(
                        ProductsCatalogContract.Intent.Init(
                            categoryId = categoryId,
                            subCategoryId = subCategoryId,
                        )
                    )
                },
            )
            is ProductsCatalogContract.State.Success -> ProductsCatalogSuccessState(
                modifier = Modifier.weight(1f),
                products = state.products,
                onProductClick = { product ->
                    viewModel.doIntent(ProductsCatalogContract.Intent.ProductClicked(product))
                },
            )
        }
    }
}
