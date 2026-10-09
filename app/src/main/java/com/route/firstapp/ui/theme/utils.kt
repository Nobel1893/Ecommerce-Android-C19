package com.route.firstapp.ui.theme

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MyPreview(
    content: @Composable ()->Unit,
){
    FirstAppTheme() {
        content()
    }
}