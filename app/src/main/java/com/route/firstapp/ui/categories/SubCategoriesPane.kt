package com.route.firstapp.ui.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.route.domain.data.Category
import com.route.domain.data.SubCategory
import com.route.firstapp.ui.theme.BrandColor
import com.route.firstapp.ui.theme.MyPreview

// Shows the selected category and picks the composable for the nested SubCategoriesState.
// Each nested state has its own file: SubCategoriesLoadingState, SubCategoriesErrorState, SubCategoriesSuccessState.
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SubCategoriesPane(modifier: Modifier = Modifier,
                      selectedCategory: Category?,
                      subCategoriesState: CategoriesContract.SubCategoriesState,
                      onSubCategoryClick: (subCategory: SubCategory) -> Unit = {},
                      onRetryClick: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (selectedCategory != null) {
            Text(
                selectedCategory.name,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = BrandColor
                ),
            )
            GlideImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(size = 16.dp)),
                model = selectedCategory.imageUrl,
                contentScale = ContentScale.Crop,
                contentDescription = selectedCategory.name,
            )
        }
        when (subCategoriesState) {
            CategoriesContract.SubCategoriesState.Loading -> SubCategoriesLoadingState()
            is CategoriesContract.SubCategoriesState.Error -> SubCategoriesErrorState(
                onRetryClick = onRetryClick,
            )
            is CategoriesContract.SubCategoriesState.Success -> SubCategoriesSuccessState(
                modifier = Modifier.weight(1f),
                subCategories = subCategoriesState.subCategories,
                onSubCategoryClick = onSubCategoryClick,
            )
        }
    }
}

private val previewCategory = Category(id = "1", name = "Electronics", imageUrl = "")

@Composable
@Preview(showBackground = true, widthDp = 260, heightDp = 500)
private fun SubCategoriesPaneSuccessPreview(){
    MyPreview {
        SubCategoriesPane(
            selectedCategory = previewCategory,
            subCategoriesState = CategoriesContract.SubCategoriesState.Success(
                subCategories = listOf(
                    SubCategory(id = "11", name = "Data Storage", categoryId = "1"),
                    SubCategory(id = "12", name = "Networking Products", categoryId = "1"),
                )
            ),
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 260, heightDp = 500)
private fun SubCategoriesPaneLoadingPreview(){
    MyPreview {
        SubCategoriesPane(
            selectedCategory = previewCategory,
            subCategoriesState = CategoriesContract.SubCategoriesState.Loading,
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 260, heightDp = 500)
private fun SubCategoriesPaneErrorPreview(){
    MyPreview {
        SubCategoriesPane(
            selectedCategory = previewCategory,
            subCategoriesState = CategoriesContract.SubCategoriesState.Error(message = ""),
        )
    }
}
