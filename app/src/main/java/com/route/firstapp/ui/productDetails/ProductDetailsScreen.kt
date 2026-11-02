package com.route.firstapp.ui.productDetails

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.route.firstapp.R
import com.route.firstapp.ui.ErrorWithRetry
import com.route.firstapp.ui.FullScreenLoading

// Entry composable only. It has no preview because it needs a Hilt ViewModel; the top bar and the
// success state have their own file and previews (ProductDetailsTopBar, ProductDetailsSuccessState),
// loading and error use the shared FullScreenLoading and ErrorWithRetry.
@Composable
fun ProductDetailsScreen(modifier: Modifier = Modifier,
                         productId: String,
                         viewModel: ProductDetailsViewModel = hiltViewModel(),
                         onNavigateBack: () -> Unit,
) {
    val context = LocalContext.current
    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        viewModel.doIntent(ProductDetailsContract.Intent.Init(productId = productId))
    }
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                ProductDetailsContract.Event.NavigateBack -> onNavigateBack()
                ProductDetailsContract.Event.ShowCartComingSoon -> Toast.makeText(
                    context,
                    R.string.cart_coming_soon,
                    Toast.LENGTH_SHORT,
                ).show()
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
        ProductDetailsTopBar(
            onBackClick = { viewModel.doIntent(ProductDetailsContract.Intent.BackClicked) },
        )
        when (val state = states.value) {
            ProductDetailsContract.State.Loading -> FullScreenLoading(
                modifier = Modifier.weight(1f),
            )
            is ProductDetailsContract.State.Error -> ErrorWithRetry(
                modifier = Modifier.padding(16.dp),
                onRetryClick = {
                    viewModel.doIntent(ProductDetailsContract.Intent.Init(productId = productId))
                },
            )
            is ProductDetailsContract.State.Success -> ProductDetailsSuccessState(
                modifier = Modifier.weight(1f),
                product = state.product,
                quantity = state.quantity,
                totalPrice = state.totalPrice,
                onIncreaseQuantityClick = {
                    viewModel.doIntent(ProductDetailsContract.Intent.IncreaseQuantityClicked)
                },
                onDecreaseQuantityClick = {
                    viewModel.doIntent(ProductDetailsContract.Intent.DecreaseQuantityClicked)
                },
                onAddToCartClick = {
                    viewModel.doIntent(ProductDetailsContract.Intent.AddToCartClicked)
                },
            )
        }
    }
}
