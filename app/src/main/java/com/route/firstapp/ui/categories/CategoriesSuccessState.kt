package com.route.firstapp.ui.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.route.domain.data.Category
import com.route.domain.data.SubCategory
import com.route.firstapp.ui.theme.BrandColor
import com.route.firstapp.ui.theme.MyPreview

@Composable
fun CategoriesSuccessState(modifier: Modifier = Modifier,
                           categories: List<Category>,
                           selectedCategory: Category?,
                           subCategoriesState: CategoriesContract.SubCategoriesState,
                           onCategoryClick: (category: Category) -> Unit = {},
                           onSubCategoryClick: (subCategory: SubCategory) -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CategoriesList(
            modifier = Modifier
                .weight(0.35f)
                .fillMaxHeight(),
            categories = categories,
            selectedCategory = selectedCategory,
            onCategoryClick = onCategoryClick,
        )
        SubCategoriesPane(
            modifier = Modifier
                .weight(0.65f)
                .fillMaxHeight(),
            selectedCategory = selectedCategory,
            subCategoriesState = subCategoriesState,
            onSubCategoryClick = onSubCategoryClick,
            onRetryClick = { selectedCategory?.let(onCategoryClick) },
        )
    }
}

@Composable
fun CategoriesList(modifier: Modifier = Modifier,
                   categories: List<Category>,
                   selectedCategory: Category?,
                   onCategoryClick: (category: Category) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier
            .clip(RoundedCornerShape(size = 16.dp))
            .background(color = BrandColor.copy(alpha = 0.1f)),
    ) {
        items(
            items = categories,
            key = { category -> category.id },
        ) { category ->
            CategoryItem(
                category = category,
                isSelected = category.id == selectedCategory?.id,
                onClick = { onCategoryClick(category) },
            )
        }
    }
}

@Composable
fun CategoryItem(modifier: Modifier = Modifier,
                 category: Category,
                 isSelected: Boolean,
                 onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(color = if (isSelected) Color.White else Color.Transparent)
            .selectable(
                selected = isSelected,
                role = Role.Tab,
                onClick = onClick,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // selection indicator
        Box(
            modifier = Modifier
                .width(6.dp)
                .fillMaxHeight()
                .background(color = if (isSelected) BrandColor else Color.Transparent)
        )
        Text(
            category.name,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 20.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = BrandColor,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            ),
        )
    }
}

private val previewCategories = listOf(
    Category(id = "1", name = "Electronics", imageUrl = ""),
    Category(id = "2", name = "Men's Fashion", imageUrl = ""),
    Category(id = "3", name = "Books", imageUrl = ""),
)

@Composable
@Preview(showBackground = true, heightDp = 640)
private fun CategoriesSuccessStatePreview(){
    MyPreview {
        CategoriesSuccessState(
            categories = previewCategories,
            selectedCategory = previewCategories.first(),
            subCategoriesState = CategoriesContract.SubCategoriesState.Success(
                subCategories = listOf(
                    SubCategory(id = "11", name = "Data Storage", categoryId = "1"),
                    SubCategory(id = "12", name = "Networking Products", categoryId = "1"),
                    SubCategory(id = "13", name = "Cameras", categoryId = "1"),
                )
            ),
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 140, heightDp = 400)
private fun CategoriesListPreview(){
    MyPreview {
        CategoriesList(
            categories = previewCategories,
            selectedCategory = previewCategories[1],
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 140)
private fun CategoryItemSelectedPreview(){
    MyPreview {
        CategoryItem(
            category = previewCategories.first(),
            isSelected = true,
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 140)
private fun CategoryItemUnselectedPreview(){
    MyPreview {
        CategoryItem(
            category = previewCategories.first(),
            isSelected = false,
        )
    }
}
