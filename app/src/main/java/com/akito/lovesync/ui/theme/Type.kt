package com.akito.lovesync.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.akito.lovesync.R

// ローカル組み込みフォント (res/font/m_plus_rounded_1c_*.ttf)
val roundedFontFamily = FontFamily(
    Font(resId = R.font.m_plus_rounded_1c_bold, weight = FontWeight.Normal),
    Font(resId = R.font.m_plus_rounded_1c_bold, weight = FontWeight.Medium),
    Font(resId = R.font.m_plus_rounded_1c_bold, weight = FontWeight.Bold),
    Font(resId = R.font.m_plus_rounded_1c_extra_bold, weight = FontWeight.ExtraBold),
    Font(resId = R.font.m_plus_rounded_1c_black, weight = FontWeight.Black)
)

// タイトル専用フォント (res/font/kyoka.ttf)
val kyokaFontFamily = FontFamily(
    Font(resId = R.font.kyoka, weight = FontWeight.Normal),
    Font(resId = R.font.kyoka, weight = FontWeight.Bold),
    Font(resId = R.font.kyoka, weight = FontWeight.ExtraBold)
)

// サブタイトル専用明朝フォント (res/font/mintyo.ttf)
val mintyoFontFamily = FontFamily(
    Font(resId = R.font.mintyo, weight = FontWeight.Normal),
    Font(resId = R.font.mintyo, weight = FontWeight.Bold)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = roundedFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)
