package com.route.firstapp.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)


val BrandColor = Color(0xFF004182)

// Figma "text color": product names, prices and screen titles
val BrandTextColor = Color(0xFF06004F)
// descriptions and secondary labels
val BrandSecondaryTextColor = BrandTextColor.copy(alpha = 0.6f)
// card, image and chip borders
val BrandBorderColor = BrandColor.copy(alpha = 0.3f)
// struck-through price before the discount
val OldPriceColor = BrandColor.copy(alpha = 0.6f)
// highlight behind selected text in text fields
val BrandSelectionColor = BrandColor.copy(alpha = 0.3f)