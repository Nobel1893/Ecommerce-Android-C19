package com.route.firstapp.ui.home.categoriesSection

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.route.domain.data.Category
import com.route.firstapp.R
import com.route.firstapp.ui.theme.BrandColor

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HomeCategoriesSuccess(categories:List<Category> ,
                          modifier: Modifier = Modifier) {
    LazyHorizontalGrid (
        modifier = Modifier.height(272.dp),
        rows = GridCells.Fixed(count = 2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(
            items = categories,
        ) { index, cat ->
            Column(
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                GlideImage(
                    model = cat.imageUrl,
                    contentDescription = "Category image",
                    modifier = Modifier.size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds,
                )
                Text(
                    cat.name ?: "",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = BrandColor,
                    )
                )
            }
        }
    }
}


@Composable
@Preview
fun HomeCategoriesLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ){
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
@Composable
@Preview
fun HomeCategoriesErrorState(modifier: Modifier = Modifier,
                             onRetryClick : ()->Unit = {}
                             ) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ){
        Text(stringResource(R.string.generic_error_message))
        ElevatedButton(
            onClick = {
                onRetryClick()
            }
        ){
            Text(stringResource(R.string.try_again))
        }
    }
}