package com.route.firstapp.ui.home.productsSection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.route.domain.data.Product
import com.route.firstapp.R
import com.route.firstapp.ui.ProductCard
import com.route.firstapp.ui.home.HomeSectionTitle

@Composable
fun HorizontalProductCardRow(modifier: Modifier = Modifier,
                             viewModel: ProductsViewModel = hiltViewModel()) {

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
        )
    }

}

@Composable
fun ProductsSuccessState(modifier: Modifier = Modifier,
                         products: List<Product>
                         ) {
    HomeSectionTitle(
        modifier = modifier.fillMaxWidth(),
        title = stringResource(R.string.most_purshable)
    )
    LazyRow(modifier = modifier,) {
        items(products.size){ index->
            ProductCard(
                products[index]
            )
        }
    }
}