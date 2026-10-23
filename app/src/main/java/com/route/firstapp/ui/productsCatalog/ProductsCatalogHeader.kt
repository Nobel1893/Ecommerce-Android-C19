package com.route.firstapp.ui.productsCatalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.route.firstapp.R
import com.route.firstapp.ui.AppSearchBar
import com.route.firstapp.ui.theme.MyPreview

// same logo and search bar as the Home tab, shown above every state
@Composable
fun ProductsCatalogHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Image(
            modifier = Modifier.height(22.dp),
            painter = painterResource(R.drawable.route_logo),
            contentDescription = stringResource(R.string.route_logo),
            contentScale = ContentScale.FillHeight,
        )
        AppSearchBar()
    }
}

@Composable
@Preview(showBackground = true)
private fun ProductsCatalogHeaderPreview(){
    MyPreview {
        ProductsCatalogHeader()
    }
}
