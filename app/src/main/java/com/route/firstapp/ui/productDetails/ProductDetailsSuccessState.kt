package com.route.firstapp.ui.productDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.route.domain.data.Product
import com.route.firstapp.R
import com.route.firstapp.ui.ProductFavoriteIcon
import com.route.firstapp.ui.theme.BrandBorderColor
import com.route.firstapp.ui.theme.BrandColor
import com.route.firstapp.ui.theme.BrandSecondaryTextColor
import com.route.firstapp.ui.theme.BrandTextColor
import com.route.firstapp.ui.theme.MyPreview

// The design's Size and Color pickers are left out: no product in the API has sizes, and
// availableColors is empty for all of them (checked 2026-10-23).
@Composable
fun ProductDetailsSuccessState(modifier: Modifier = Modifier,
                               product: Product,
                               quantity: Int,
                               totalPrice: Int?,
                               onIncreaseQuantityClick: () -> Unit = {},
                               onDecreaseQuantityClick: () -> Unit = {},
                               onAddToCartClick: () -> Unit = {},
) {
    // some products only have a cover image
    val images = remember(product) { product.images.ifEmpty { listOfNotNull(product.imageUrl) } }
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(state = rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ProductImagesPager(
                images = images,
                contentDescription = product.name,
            )
            ProductNameAndPrice(
                modifier = Modifier.padding(top = 8.dp),
                name = product.name ?: "",
                price = product.finalPrice,
            )
            ProductSalesRow(
                soldCount = product.soldCount,
                ratingsAverage = product.ratingsAverage,
                ratingsQuantity = product.ratingsQuantity,
                quantity = quantity,
                onIncreaseQuantityClick = onIncreaseQuantityClick,
                onDecreaseQuantityClick = onDecreaseQuantityClick,
            )
            if (!product.description.isNullOrBlank()) {
                ProductDescription(
                    description = product.description,
                )
            }
        }
        ProductDetailsBottomBar(
            totalPrice = totalPrice,
            onAddToCartClick = onAddToCartClick,
        )
    }
}

// the heart is decorative until the app has sign-in (the wishlist API needs a token)
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductImagesPager(modifier: Modifier = Modifier,
                       images: List<String>,
                       contentDescription: String?,
) {
    val pagerState = rememberPagerState(pageCount = { images.size })
    val shape = RoundedCornerShape(size = 15.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(shape)
            .border(width = 1.dp, color = BrandBorderColor, shape = shape),
    ) {
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
        ) { page ->
            GlideImage(
                modifier = Modifier.fillMaxSize(),
                model = images[page],
                contentScale = ContentScale.Crop,
                contentDescription = contentDescription,
            )
        }
        ProductFavoriteIcon(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
        )
        if (images.size > 1) {
            ProductImagesIndicator(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                pageCount = images.size,
                currentPage = pagerState.currentPage,
            )
        }
    }
}

@Composable
fun ProductImagesIndicator(modifier: Modifier = Modifier,
                           pageCount: Int,
                           currentPage: Int,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        repeat(pageCount) { page ->
            if (page == currentPage) {
                Box(
                    modifier = Modifier
                        .size(width = 30.dp, height = 7.dp)
                        .background(color = BrandColor, shape = RoundedCornerShape(size = 30.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .background(color = Color.White, shape = CircleShape)
                        .border(width = 1.dp, color = BrandColor, shape = CircleShape)
                )
            }
        }
    }
}

@Composable
fun ProductNameAndPrice(modifier: Modifier = Modifier,
                        name: String,
                        price: Int?,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            name,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleMedium.copy(color = BrandTextColor),
        )
        if (price != null) {
            Text(
                stringResource(R.string.price_egp, price),
                style = MaterialTheme.typography.titleMedium.copy(color = BrandTextColor),
            )
        }
    }
}

@Composable
fun ProductSalesRow(modifier: Modifier = Modifier,
                    soldCount: Int?,
                    ratingsAverage: Double?,
                    ratingsQuantity: Int?,
                    quantity: Int,
                    onIncreaseQuantityClick: () -> Unit = {},
                    onDecreaseQuantityClick: () -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // the weighted part is measured after the stepper, so on narrow phones the rating
        // wraps under the chip instead of squeezing the stepper's buttons
        FlowRow(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            itemVerticalAlignment = Alignment.CenterVertically,
        ) {
            if (soldCount != null) {
                SoldCountChip(soldCount = soldCount)
            }
            if (ratingsAverage != null) {
                ProductRating(
                    ratingsAverage = ratingsAverage,
                    ratingsQuantity = ratingsQuantity ?: 0,
                )
            }
        }
        QuantityStepper(
            quantity = quantity,
            onIncreaseClick = onIncreaseQuantityClick,
            onDecreaseClick = onDecreaseQuantityClick,
        )
    }
}

@Composable
fun SoldCountChip(modifier: Modifier = Modifier,
                  soldCount: Int,
) {
    Text(
        stringResource(R.string.sold_count, soldCount),
        modifier = modifier
            .border(width = 1.dp, color = BrandBorderColor, shape = RoundedCornerShape(size = 20.dp))
            .padding(horizontal = 16.dp, vertical = 7.dp),
        style = MaterialTheme.typography.labelLarge.copy(color = BrandTextColor),
    )
}

@Composable
fun ProductRating(modifier: Modifier = Modifier,
                  ratingsAverage: Double,
                  ratingsQuantity: Int,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_star),
            // keep the star's own colors
            tint = Color.Unspecified,
            contentDescription = null,
        )
        Text(
            stringResource(R.string.rating_with_count, ratingsAverage, ratingsQuantity),
            style = MaterialTheme.typography.bodyMedium.copy(color = BrandTextColor),
        )
    }
}

@Composable
fun QuantityStepper(modifier: Modifier = Modifier,
                    quantity: Int,
                    onIncreaseClick: () -> Unit = {},
                    onDecreaseClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .height(42.dp)
            .background(color = BrandColor, shape = RoundedCornerShape(size = 20.dp))
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        IconButton(
            modifier = Modifier.size(42.dp),
            onClick = onDecreaseClick,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_quantity_minus),
                tint = Color.White,
                contentDescription = stringResource(R.string.decrease_quantity),
            )
        }
        Text(
            // formatted so Arabic shows Arabic-Indic digits like the prices
            stringResource(R.string.quantity_count, quantity),
            // keeps the buttons in place when the count gets another digit
            modifier = Modifier.widthIn(min = 22.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
        )
        IconButton(
            modifier = Modifier.size(42.dp),
            onClick = onIncreaseClick,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_quantity_plus),
                tint = Color.White,
                contentDescription = stringResource(R.string.increase_quantity),
            )
        }
    }
}

private const val COLLAPSED_DESCRIPTION_LINES = 3

@Composable
fun ProductDescription(modifier: Modifier = Modifier,
                       description: String,
) {
    // only changes how much text is visible, so it stays here rather than in the ViewModel
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var hasHiddenText by remember { mutableStateOf(false) }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            stringResource(R.string.description),
            style = MaterialTheme.typography.titleMedium.copy(color = BrandTextColor),
        )
        Text(
            description,
            maxLines = if (isExpanded) Int.MAX_VALUE else COLLAPSED_DESCRIPTION_LINES,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium.copy(color = BrandSecondaryTextColor),
            onTextLayout = { layout ->
                if (!isExpanded) hasHiddenText = layout.hasVisualOverflow
            },
        )
        if (isExpanded || hasHiddenText) {
            Text(
                stringResource(if (isExpanded) R.string.read_less else R.string.read_more),
                modifier = Modifier.clickable { isExpanded = !isExpanded },
                style = MaterialTheme.typography.bodyMedium.copy(color = BrandTextColor),
            )
        }
    }
}

@Composable
fun ProductDetailsBottomBar(modifier: Modifier = Modifier,
                            totalPrice: Int?,
                            onAddToCartClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                stringResource(R.string.total_price),
                style = MaterialTheme.typography.titleMedium.copy(color = BrandSecondaryTextColor),
            )
            if (totalPrice != null) {
                Text(
                    stringResource(R.string.price_egp, totalPrice),
                    style = MaterialTheme.typography.titleMedium.copy(color = BrandTextColor),
                )
            }
        }
        Button(
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            onClick = onAddToCartClick,
            shape = RoundedCornerShape(size = 20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandColor,
                contentColor = Color.White,
            ),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add_to_cart),
                tint = Color.White,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                stringResource(R.string.add_to_cart),
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}

private val previewProduct = Product(
    id = "6428ebc6dc1175abc65ca0b9",
    name = "Woman Shawl",
    imageUrl = "https://ecommerce.routemisr.com/Route-Academy-products/1680403397402-cover.jpeg",
    description = "Material Polyester Blend\nColour Name Multicolour\nDepartment Women\n" +
            "A soft, lightweight shawl that keeps you warm on cool evenings and goes with any outfit.",
    price = 499,
    priceAfterDiscount = 349,
    ratingsAverage = 4.1,
    ratingsQuantity = 47,
    soldCount = 2973,
    availableQuantity = 220,
    images = listOf(
        "https://ecommerce.routemisr.com/Route-Academy-products/1680403397482-1.jpeg",
        "https://ecommerce.routemisr.com/Route-Academy-products/1680403397482-2.jpeg",
        "https://ecommerce.routemisr.com/Route-Academy-products/1680403397483-3.jpeg",
    ),
)

@Composable
@Preview(showBackground = true, widthDp = 430, heightDp = 880)
private fun ProductDetailsSuccessStatePreview(){
    MyPreview {
        ProductDetailsSuccessState(
            product = previewProduct,
            quantity = 2,
            totalPrice = 698,
        )
    }
}

// Glide doesn't load network images in previews, so the pager shows an empty box
@Composable
@Preview(showBackground = true, widthDp = 430)
private fun ProductImagesPagerPreview(){
    MyPreview {
        ProductImagesPager(
            images = previewProduct.images,
            contentDescription = previewProduct.name,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ProductImagesIndicatorPreview(){
    MyPreview {
        ProductImagesIndicator(
            pageCount = 3,
            currentPage = 0,
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 430)
private fun ProductNameAndPricePreview(){
    MyPreview {
        ProductNameAndPrice(
            name = "Woman Shawl",
            price = 349,
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 430)
private fun ProductSalesRowPreview(){
    MyPreview {
        ProductSalesRow(
            soldCount = 2973,
            ratingsAverage = 4.1,
            ratingsQuantity = 47,
            quantity = 1,
        )
    }
}

// a 360 dp phone minus the screen padding, where the rating wraps under the chip
@Composable
@Preview(showBackground = true, widthDp = 328)
private fun ProductSalesRowNarrowPreview(){
    MyPreview {
        ProductSalesRow(
            soldCount = 5078,
            ratingsAverage = 4.3,
            ratingsQuantity = 68,
            quantity = 12,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun SoldCountChipPreview(){
    MyPreview {
        SoldCountChip(soldCount = 3230)
    }
}

@Composable
@Preview(showBackground = true)
private fun ProductRatingPreview(){
    MyPreview {
        ProductRating(
            ratingsAverage = 4.8,
            ratingsQuantity = 7500,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun QuantityStepperPreview(){
    MyPreview {
        QuantityStepper(quantity = 1)
    }
}

@Composable
@Preview(showBackground = true, widthDp = 430)
private fun ProductDescriptionPreview(){
    MyPreview {
        ProductDescription(
            description = previewProduct.description ?: "",
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 430)
private fun ProductDetailsBottomBarPreview(){
    MyPreview {
        ProductDetailsBottomBar(
            totalPrice = 3500,
        )
    }
}
