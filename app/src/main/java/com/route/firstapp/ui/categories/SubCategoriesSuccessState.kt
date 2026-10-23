package com.route.firstapp.ui.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.route.domain.data.SubCategory
import com.route.firstapp.R
import com.route.firstapp.ui.theme.BrandColor
import com.route.firstapp.ui.theme.MyPreview

@Composable
fun SubCategoriesSuccessState(modifier: Modifier = Modifier,
                              subCategories: List<SubCategory>,
                              onSubCategoryClick: (subCategory: SubCategory) -> Unit = {},
) {
    if (subCategories.isEmpty()) {
        Text(
            stringResource(R.string.no_sub_categories),
            modifier = modifier,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = BrandColor
            ),
        )
    } else {
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(count = 3),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(
                items = subCategories,
                key = { subCategory -> subCategory.id },
            ) { subCategory ->
                SubCategoryItem(
                    subCategory = subCategory,
                    onClick = { onSubCategoryClick(subCategory) },
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SubCategoryItem(modifier: Modifier = Modifier,
                    subCategory: SubCategory,
                    onClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val imageModifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(size = 8.dp))
            .border(
                width = 1.dp,
                color = BrandColor.copy(alpha = 0.3f),
                shape = RoundedCornerShape(size = 8.dp),
            )
        if (subCategory.imageUrl == null) {
            SubCategoryImagePlaceholder(
                modifier = imageModifier,
                name = subCategory.name,
            )
        } else {
            GlideImage(
                modifier = imageModifier,
                model = subCategory.imageUrl,
                contentScale = ContentScale.Crop,
                contentDescription = subCategory.name,
                loading = placeholder { SubCategoryImagePlaceholder(name = subCategory.name) },
                failure = placeholder { SubCategoryImagePlaceholder(name = subCategory.name) },
            )
        }
        Text(
            subCategory.name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall.copy(
                color = BrandColor,
                // API names are longer than the design's, hyphenate words wider than a tile
                hyphens = Hyphens.Auto,
            ),
        )
    }
}

// shown while the API has no subcategory images, or when an image fails to load
@Composable
fun SubCategoryImagePlaceholder(modifier: Modifier = Modifier,
                                name: String,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = BrandColor.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            name.take(1).uppercase(),
            style = MaterialTheme.typography.titleSmall.copy(
                color = BrandColor
            ),
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 260, heightDp = 400)
private fun SubCategoriesSuccessStatePreview(){
    MyPreview {
        SubCategoriesSuccessState(
            subCategories = listOf(
                SubCategory(id = "11", name = "Data Storage", categoryId = "1"),
                SubCategory(id = "12", name = "Networking Products", categoryId = "1"),
                SubCategory(id = "13", name = "Cameras & Accessories", categoryId = "1"),
                SubCategory(id = "14", name = "Video Games", categoryId = "1"),
            )
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 260)
private fun SubCategoriesSuccessStateEmptyPreview(){
    MyPreview {
        SubCategoriesSuccessState(
            subCategories = listOf()
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 90)
private fun SubCategoryItemPreview(){
    MyPreview {
        SubCategoryItem(
            subCategory = SubCategory(id = "12", name = "Networking Products", categoryId = "1"),
        )
    }
}

// Glide doesn't load network images in previews, so this shows the loading placeholder
@Composable
@Preview(showBackground = true, widthDp = 90)
private fun SubCategoryItemWithImagePreview(){
    MyPreview {
        SubCategoryItem(
            subCategory = SubCategory(
                id = "12",
                name = "Networking Products",
                categoryId = "1",
                imageUrl = "https://example.com/sub-category.png",
            ),
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 90, heightDp = 90)
private fun SubCategoryImagePlaceholderPreview(){
    MyPreview {
        SubCategoryImagePlaceholder(name = "Networking Products")
    }
}
