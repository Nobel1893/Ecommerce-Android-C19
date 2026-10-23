package com.route.firstapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.route.domain.data.Product
import com.route.firstapp.R
import com.route.firstapp.ui.theme.BrandBorderColor
import com.route.firstapp.ui.theme.BrandColor
import com.route.firstapp.ui.theme.BrandTextColor
import com.route.firstapp.ui.theme.MyPreview
import com.route.firstapp.ui.theme.OldPriceColor


// the heart and plus icons are decorative until the app has sign-in (wishlist and cart need a token)
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductCard(modifier: Modifier = Modifier,
                product: Product,
                onProductClick:(product: Product)-> Unit = {}){
    val shape = RoundedCornerShape(size = 15.dp)
    Column(
        modifier = modifier
            .clip(shape)
            .border(width = 2.dp, color = BrandBorderColor, shape = shape)
            .background(color = Color.White)
            .clickable {
                onProductClick(product)
            }
    ){
        Box {
            GlideImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp),
                model = product.imageUrl,
                contentScale = ContentScale.Crop,
                // the name is the Text right below, TalkBack would read it twice
                contentDescription = null,
            )
            ProductFavoriteIcon(
                modifier = Modifier
                    .align(alignment = Alignment.TopEnd)
                    .padding(8.dp),
            )
        }
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                product.name ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium.copy(color = BrandTextColor),
            )
            Text(
                product.description ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium.copy(color = BrandTextColor),
            )
            ProductCardPrice(
                modifier = Modifier.padding(top = 8.dp),
                product = product,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    stringResource(R.string.review_rating, product.ratingsAverage ?: 0.0),
                    style = MaterialTheme.typography.bodySmall.copy(color = BrandTextColor),
                )
                Icon(
                    modifier = Modifier.padding(start = 4.dp),
                    painter = painterResource(R.drawable.ic_star),
                    // keep the star's own colors
                    tint = Color.Unspecified,
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(R.drawable.ic_add_circle),
                    tint = BrandColor,
                    contentDescription = null,
                )
            }
        }
    }
}

// the discounted price, followed by the struck-through original price when there's a discount
@Composable
fun ProductCardPrice(modifier: Modifier = Modifier,
                     product: Product,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        product.finalPrice?.let { finalPrice ->
            Text(
                stringResource(R.string.price_egp, finalPrice),
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium.copy(color = BrandTextColor),
            )
        }
        if (product.priceAfterDiscount != null && product.price != null) {
            // on narrow cards the old price is cut short rather than wrapping and making the card taller
            Text(
                stringResource(R.string.old_price_egp, product.price),
                modifier = Modifier.weight(1f, fill = false),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = OldPriceColor,
                    textDecoration = TextDecoration.LineThrough,
                ),
            )
        }
    }
}

@Composable
fun ProductFavoriteIcon(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(30.dp)
            .shadow(elevation = 4.dp, shape = CircleShape)
            .background(color = Color.White, shape = CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_fav),
            tint = BrandColor,
            contentDescription = null,
        )
    }
}

// a spinner in the middle of all the space it's given, for a screen that is loading
@Composable
fun FullScreenLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ){
        CircularProgressIndicator()
    }
}

// the Scaffolds set a white content color, so the text here needs an explicit color
@Composable
fun ErrorWithRetry(modifier: Modifier = Modifier,
                   onRetryClick: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            stringResource(R.string.generic_error_message),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = BrandColor
            ),
        )
        ElevatedButton(onClick = onRetryClick) {
            Text(stringResource(R.string.try_again))
        }
    }
}

private val previewProduct = Product(
    id = "6428ebc6dc1175abc65ca0b9",
    name = "Woman Shawl",
    imageUrl = "https://ecommerce.routemisr.com/Route-Academy-products/1680403397402-cover.jpeg",
    description = "Material Polyester Blend, Colour Name Multicolour, Department Women",
    price = 499,
    priceAfterDiscount = 349,
    ratingsAverage = 4.1,
    ratingsQuantity = 47,
)

@Composable
@Preview(showBackground = true, widthDp = 191)
private fun ProductCardPreview(){
    MyPreview {
        ProductCard(
            product = previewProduct,
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 191)
private fun ProductCardNoDiscountPreview(){
    MyPreview {
        ProductCard(
            product = previewProduct.copy(priceAfterDiscount = null),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ProductCardPricePreview(){
    MyPreview {
        ProductCardPrice(
            product = previewProduct,
        )
    }
}

// the inner width of a Home row card, with the longest prices in the API
@Composable
@Preview(showBackground = true, widthDp = 144)
private fun ProductCardPriceNarrowPreview(){
    MyPreview {
        ProductCardPrice(
            product = previewProduct.copy(price = 20999, priceAfterDiscount = 16799),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ProductCardPriceNoDiscountPreview(){
    MyPreview {
        ProductCardPrice(
            product = previewProduct.copy(priceAfterDiscount = null),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ProductFavoriteIconPreview(){
    MyPreview {
        ProductFavoriteIcon(
            modifier = Modifier.padding(8.dp),
        )
    }
}

@Composable
@Preview(showBackground = true, heightDp = 400)
private fun FullScreenLoadingPreview(){
    MyPreview {
        FullScreenLoading()
    }
}

@Composable
@Preview(showBackground = true)
private fun ErrorWithRetryPreview(){
    MyPreview {
        ErrorWithRetry(
            onRetryClick = {},
        )
    }
}
