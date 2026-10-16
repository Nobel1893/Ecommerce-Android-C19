package com.route.firstapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.route.firstapp.R
import com.route.firstapp.ui.ProductCard
import com.route.firstapp.ui.theme.BrandColor
import com.route.firstapp.ui.theme.MyPreview

@Composable
fun HomeSectionTitle(
    modifier: Modifier = Modifier,
    title: String,
    viewAllText : String = stringResource(R.string.view_all)
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(title,
            style = MaterialTheme.typography.titleSmall.copy(
                color = BrandColor
            ),
            )
        Text(viewAllText,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = BrandColor
            ),)
    }
}

@Composable
@Preview
private fun HomeSectionTitlePreview(){
    MyPreview {
        HomeSectionTitle(
            title = "Categories",
        )
    }
}
