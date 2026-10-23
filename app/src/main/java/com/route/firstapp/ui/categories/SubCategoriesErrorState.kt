package com.route.firstapp.ui.categories

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.route.firstapp.ui.theme.MyPreview

// same UI as the screen-level error, retrying reloads only the selected category's children
@Composable
fun SubCategoriesErrorState(modifier: Modifier = Modifier,
                            onRetryClick: () -> Unit = {},
) {
    CategoriesErrorState(
        modifier = modifier,
        onRetryClick = onRetryClick,
    )
}

@Composable
@Preview(showBackground = true, widthDp = 260)
private fun SubCategoriesErrorStatePreview(){
    MyPreview {
        SubCategoriesErrorState()
    }
}
