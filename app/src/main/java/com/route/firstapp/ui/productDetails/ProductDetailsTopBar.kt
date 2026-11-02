package com.route.firstapp.ui.productDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.route.firstapp.R
import com.route.firstapp.ui.theme.BrandColor
import com.route.firstapp.ui.theme.BrandTextColor
import com.route.firstapp.ui.theme.MyPreview

// shown above every state so Back always works
@Composable
fun ProductDetailsTopBar(modifier: Modifier = Modifier,
                         onBackClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 4.dp),
    ) {
        IconButton(
            modifier = Modifier.align(Alignment.CenterStart),
            onClick = onBackClick,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                tint = BrandColor,
                contentDescription = stringResource(R.string.back),
            )
        }
        Text(
            stringResource(R.string.product_details),
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.titleLarge.copy(
                color = BrandTextColor
            ),
        )
        // there are no search or cart screens yet, so these are decorative like the Home tab's cart icon
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                tint = BrandColor,
                contentDescription = null,
            )
            Icon(
                painter = painterResource(R.drawable.ic_cart),
                tint = BrandColor,
                contentDescription = null,
            )
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 430)
private fun ProductDetailsTopBarPreview(){
    MyPreview {
        ProductDetailsTopBar()
    }
}
