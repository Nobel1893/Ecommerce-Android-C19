package com.route.firstapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import com.route.firstapp.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.5.sp
    ) ,
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.5.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.sp
),
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

// Font of the auth screens (Figma sign in / sign up). Not part of Typography, so Home keeps its fonts.
val Poppins = FontFamily(
    Font(R.font.poppins_light, FontWeight.Light),
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
)

// Figma gives every auth text an 18 line height with the glyphs centred in it, and its y positions
// are measured on those 18-high boxes. Trim.Both + Mode.Tight keeps the box at exactly the line
// height even when the font is taller (the default Mode.Fixed pads it back to the font's height);
// the glyphs still draw outside the box, as they do in Figma.
private val AuthTextStyle = TextStyle(
    fontFamily = Poppins,
    letterSpacing = (-0.165).sp,
    lineHeight = 18.sp,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.Both,
        mode = LineHeightStyle.Mode.Tight,
    ),
)

val AuthTitleTextStyle = AuthTextStyle.copy(
    fontWeight = FontWeight.SemiBold,
    fontSize = 24.sp,
    color = Color.White,
)

val AuthSubtitleTextStyle = AuthTextStyle.copy(
    fontWeight = FontWeight.Light,
    fontSize = 16.sp,
    color = Color.White,
)

val AuthFieldLabelTextStyle = AuthTextStyle.copy(
    fontWeight = FontWeight.Medium,
    fontSize = 18.sp,
    color = Color.White,
)

// Typed text and placeholder. No fixed line height: a text field clips glyphs taller than its line.
val AuthInputTextStyle = TextStyle(
    fontFamily = Poppins,
    fontWeight = FontWeight.Light,
    fontSize = 18.sp,
    letterSpacing = (-0.165).sp,
)

val AuthLinkTextStyle = AuthTextStyle.copy(
    fontWeight = FontWeight.Normal,
    fontSize = 18.sp,
    color = Color.White,
)

val AuthPromptTextStyle = AuthTextStyle.copy(
    fontWeight = FontWeight.Medium,
    fontSize = 18.sp,
    color = Color.White,
)

val AuthButtonTextStyle = AuthTextStyle.copy(
    fontWeight = FontWeight.SemiBold,
    fontSize = 20.sp,
    color = BrandColor,
)

val AuthFieldErrorTextStyle = TextStyle(
    fontFamily = Poppins,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    letterSpacing = (-0.165).sp,
    color = AuthErrorColor,
)