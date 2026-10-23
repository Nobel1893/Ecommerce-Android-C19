package com.route.firstapp.ui.home.productsSection

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.route.domain.data.Product
import com.route.domain.data.ProductsSort
import com.route.firstapp.R
import com.route.firstapp.ui.AppRoutes
import com.route.firstapp.ui.ProductCard
import com.route.firstapp.ui.home.HomeSectionTitle

@Composable
fun HorizontalProductCardRow(modifier: Modifier = Modifier,
                             sort: ProductsSort,
                             viewModel: ProductsViewModel = hiltViewModel(
                                 key = sort.name
                             ),
                             onNavigate:(AppRoutes)-> Unit,
                             ) {

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        Log.e("Horinontal Products Card", sort.name)
        viewModel.doIntent(
            ProductsSectionContract.Intent.Init(sort = sort)
        )
    }
    // one-shot events are handled in an effect, not the composition body, so a recomposition
    // can't navigate again
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is ProductsSectionContract.Event.NavigateToProductDetails -> {
                    onNavigate(AppRoutes.ProductDetails(productId = event.productId))
                }
            }
        }
    }
    val states = viewModel.states.collectAsStateWithLifecycle()

    when (val state  = states.value) {
        is ProductsSectionContract.State.Error -> {
            Column {
                Text("Error , Something went wrong")
                ElevatedButton(onClick = {
                }) {
                    Text("Try Again")
                }
            }
        }
        ProductsSectionContract.State.Loading -> {
            Box {
                CircularProgressIndicator()
            }
        }
        is ProductsSectionContract.State.Success -> ProductsSuccessState(
            products = state.products,
            onProductClick = {product ->
                viewModel.doIntent(ProductsSectionContract.Intent.ProductClicked(product))
            }
        )
    }

}

@Composable
fun ProductsSuccessState(modifier: Modifier = Modifier,
                         products: List<Product>,
                         onProductClick:(product: Product)-> Unit = {}
                         ) {
    HomeSectionTitle(
        modifier = modifier.fillMaxWidth(),
        title = stringResource(R.string.most_purshable)
    )
    LazyRow(modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(products.size){ index->
            ProductCard(
                modifier = Modifier.width(160.dp),
                product = products[index],
                onProductClick = onProductClick,
            )
        }
    }
}