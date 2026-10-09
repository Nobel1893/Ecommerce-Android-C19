package com.route.firstapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.route.firstapp.R
import com.route.firstapp.ui.home.categoriesSection.HomeCategoriesSection
import com.route.firstapp.ui.theme.BrandColor
import com.route.firstapp.ui.theme.MyPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Scaffold(
        contentColor = Color.White,
        topBar = {
            TopAppBar(title = {
                Image(
                    modifier = Modifier.height(24.dp),
                    painter = painterResource(R.drawable.route_logo),
                    contentDescription = "logo",
                    contentScale = ContentScale.FillHeight
                )
            }
            )
        }
    ){paddingValues -> 
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(state = rememberScrollState())
            ,
            verticalArrangement = Arrangement.spacedBy(16.dp),

        ) {
            HomeSearchBar()
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.FillBounds,
                painter = painterResource(R.drawable.home_banner),
                contentDescription = "home banner"
            )


            HomeCategoriesSection()

            HorizontalProductCardRow(size = 20)
        }
    }
}


@Composable
fun HomeSearchBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        OutlinedTextField(
            modifier = Modifier
                .background(color = Color.White,)
                .weight(1f, fill = true),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = BrandColor,
            ),
            prefix = {
                Icon(imageVector = Icons.Default.Search,
                    contentDescription = "search")
            },

            shape =RoundedCornerShape(size = 50.dp),
            state = TextFieldState(initialText = "Search Here"),
        )
        Icon(
            modifier = Modifier.width(24.dp),
            imageVector = Icons.Default.ShoppingCart,
            tint = BrandColor,
            contentDescription = "Cart")
    }
}
@Composable
@Preview
private fun HomeScreenPreview(){
    MyPreview {
        HomeScreen(
        )
    }
}
