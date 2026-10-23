package com.route.firstapp.ui.productsCatalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.route.domain.data.Product
import com.route.firstapp.R
import com.route.firstapp.ui.ProductCard
import com.route.firstapp.ui.theme.BrandColor
import com.route.firstapp.ui.theme.MyPreview

@Composable
fun ProductsCatalogSuccessState(modifier: Modifier = Modifier,
                                products: List<Product>,
                                onProductClick: (product: Product) -> Unit = {},
) {
    if (products.isEmpty()) {
        Text(
            stringResource(R.string.no_products),
            modifier = modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = BrandColor
            ),
        )
    } else {
        LazyVerticalGrid(
            modifier = modifier,
            // 2 columns on phones in portrait (as in the design), more in landscape
            columns = GridCells.Adaptive(minSize = 150.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            itemsIndexed(
                items = products,
                // the index only stands in for a missing id, an Int can't clash with a String id
                key = { index, product -> product.id ?: index },
            ) { _, product ->
                ProductCard(
                    product = product,
                    onProductClick = onProductClick,
                )
            }
        }
    }
}

private val previewProducts = listOf(
    Product(
        id = "1",
        name = "Woman Shawl",
        description = "Material Polyester Blend, Department Women",
        price = 499,
        priceAfterDiscount = 349,
        ratingsAverage = 4.1,
    ),
    Product(
        id = "2",
        name = "Woman Standart Fit Knitted Dress",
        description = "Material Cotton, Colour Name Black",
        price = 499,
        ratingsAverage = 4.4,
    ),
    Product(
        id = "3",
        name = "Logo T-Shirt Green",
        description = "Material Cotton, Department Men",
        price = 744,
        priceAfterDiscount = 379,
        ratingsAverage = 3.6,
    ),
)

@Composable
@Preview(showBackground = true, widthDp = 430, heightDp = 600)
private fun ProductsCatalogSuccessStatePreview(){
    MyPreview {
        ProductsCatalogSuccessState(
            products = previewProducts,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ProductsCatalogSuccessStateEmptyPreview(){
    MyPreview {
        ProductsCatalogSuccessState(
            products = listOf(),
        )
    }
}
