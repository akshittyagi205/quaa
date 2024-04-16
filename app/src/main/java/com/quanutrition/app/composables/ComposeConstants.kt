package com.quanutrition.app.composables

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.quanutrition.app.R

object ComposeConstants {
    private val defaultFont = Font(R.font.defaultfont)
    private val defaultFontFamily = FontFamily(defaultFont)

    private val boldFont = Font(R.font.roboto_medium)
    private val boldFontFamily= FontFamily(boldFont)


    val smallTextDefault = TextStyle(fontSize = 16.sp, fontFamily = defaultFontFamily)
    val verySmallTextDefault = TextStyle(fontSize = 14.sp, fontFamily = defaultFontFamily)
    val tinyTextDefault = TextStyle(fontSize = 12.sp, fontFamily = defaultFontFamily)
    val mediumTextDefault = TextStyle(fontSize = 18.sp, fontFamily = defaultFontFamily)
    val largeTextDefault = TextStyle(fontSize = 20.sp, fontFamily = defaultFontFamily)

    val mediumBoldText = TextStyle(fontSize = 18.sp, fontFamily = boldFontFamily)
    val verySmallBoldText = TextStyle(fontSize = 14.sp, fontFamily = boldFontFamily)
    val tinyTextBoldDefault = TextStyle(fontSize = 10.sp, fontFamily = boldFontFamily)
    val smallBoldText = TextStyle(fontSize = 16.sp, fontFamily = boldFontFamily)
    val largeBoldText = TextStyle(fontSize = 20.sp, fontFamily = boldFontFamily)
    val veryLargeBoldText = TextStyle(fontSize = 24.sp, fontFamily = boldFontFamily)
}