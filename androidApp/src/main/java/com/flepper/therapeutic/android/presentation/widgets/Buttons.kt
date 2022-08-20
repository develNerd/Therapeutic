package com.flepper.therapeutic.android.presentation.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.flepper.therapeutic.android.presentation.theme.*

@Composable
fun RoundedCornerButton(
    text: String,
    backgroundColor: Color = MaterialTheme.colors.primary,
    contentColor: Color = MaterialTheme.colors.onSurface,
    modifier: Modifier = Modifier,
    isEnabled:Boolean = true,
    roundness:Int = button_roundness,
    textPadding:Dp = smallPadding,
    isLoading:Boolean = false,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(backgroundColor, contentColor),
        shape = RoundedCornerShape(roundness),
        modifier = modifier,
        enabled = isEnabled
    ) {
        if (!isLoading){
            MediumTextBold(text = text, modifier = Modifier.padding(textPadding), color =  contentColor)
        }else{
            CircularProgressIndicator(color = Color.White)
        }
    }
}

@Composable
fun RoundedCornerButtonWithIcon(
    text: String,
    backgroundColor: Color = MaterialTheme.colors.primary,
    contentColor: Color = MaterialTheme.colors.onSurface,
    modifier: Modifier = Modifier,
    isEnabled:Boolean = true,
    icon:ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(backgroundColor, contentColor),
        shape = RoundedCornerShape(button_roundness),
        modifier = modifier,
        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
        enabled = isEnabled
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            RegularText(text = text, modifier = Modifier
                .padding(spacing3dp)
                .align(Alignment.CenterStart), color =  contentColor)
            Icon(icon, contentDescription = "",modifier = Modifier.align(
                Alignment.CenterEnd))
        }
    }
}

@Composable
fun RoundedOutlineButton(
    text: String,
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = colorWhite,
    modifier: Modifier = Modifier,
    isEnabled:Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(backgroundColor, contentColor),
        shape = RoundedCornerShape(100),
        border = BorderStroke(width = size2dp, color = contentColor),
        modifier = modifier,
        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
        enabled = isEnabled
    ) {
        RegularText(text = text, modifier = Modifier.padding(smallPadding), color =  contentColor, textAlign = TextAlign.Center)
    }
}