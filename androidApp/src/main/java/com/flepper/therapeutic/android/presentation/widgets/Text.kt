package com.flepper.therapeutic.android.presentation.widgets

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.flepper.therapeutic.android.presentation.theme.LocalFontThemes
import com.flepper.therapeutic.android.presentation.theme.large_bold_text_size
import com.flepper.therapeutic.android.presentation.theme.medium_bold_text_size
import com.flepper.therapeutic.android.presentation.theme.textGray

@Composable
fun LargeBoldText(text:String?, modifier: Modifier = Modifier, color: Color = MaterialTheme.colors.onSurface,fontSize:TextUnit = large_bold_text_size, lineHeight: TextUnit = 50.sp){
    Text(text ?: "", fontFamily = LocalFontThemes.current.fontFamily , color = color,fontSize = fontSize, fontWeight =  FontWeight.Bold)
}

@Composable
fun MediumTextBold(text:String?, modifier: Modifier = Modifier, textSize: TextUnit = medium_bold_text_size, color: Color = MaterialTheme.colors.onSurface, textAlign: TextAlign = TextAlign.Start, lineHeight: TextUnit = TextUnit.Unspecified,fontWeight: FontWeight = FontWeight.Medium){
    Text(text ?: "", modifier = modifier , textAlign = textAlign,fontFamily = LocalFontThemes.current.fontFamily , color = color,fontSize = textSize, fontWeight = fontWeight, lineHeight = lineHeight)
}

@Composable
fun RegularText(text:String?, modifier: Modifier = Modifier, color: Color = MaterialTheme.colors.onSurface, size: TextUnit = medium_bold_text_size, textAlign: TextAlign = TextAlign.Start){
    Text(text ?: "", modifier = modifier ,fontFamily = LocalFontThemes.current.fontFamily , color = color,fontSize = size, fontWeight =  FontWeight.Normal, textAlign = textAlign)
}