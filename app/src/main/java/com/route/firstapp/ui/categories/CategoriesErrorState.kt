package com.route.firstapp.ui.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.route.firstapp.R
import com.route.firstapp.ui.theme.BrandColor
import com.route.firstapp.ui.theme.MyPreview

// the home Scaffold sets a white content color, so text here needs an explicit color
@Composable
fun CategoriesErrorState(modifier: Modifier = Modifier,
                         onRetryClick: () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            stringResource(R.string.generic_error_message),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = BrandColor
            ),
        )
        ElevatedButton(onClick = onRetryClick) {
            Text(stringResource(R.string.try_again))
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun CategoriesErrorStatePreview(){
    MyPreview {
        CategoriesErrorState()
    }
}
