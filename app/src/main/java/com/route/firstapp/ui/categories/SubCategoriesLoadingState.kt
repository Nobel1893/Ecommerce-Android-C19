package com.route.firstapp.ui.categories

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.route.firstapp.ui.theme.MyPreview

@Composable
fun SubCategoriesLoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ){
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 260)
private fun SubCategoriesLoadingStatePreview(){
    MyPreview {
        SubCategoriesLoadingState()
    }
}
