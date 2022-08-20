package com.flepper.therapeutic.android.presentation.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.flepper.therapeutic.android.presentation.theme.*

@Composable
fun OutLineEdittext(modifier: Modifier = Modifier, hint:String, text:String, backgroundColor : Color = Color.Transparent,stroke: BorderStroke, onTextChanged:(String) -> Unit){

    Box(modifier = modifier.fillMaxWidth().background(backgroundColor, shape = RoundedCornerShape(outlineEdittextRoundness)).border(
        stroke, shape = RoundedCornerShape(outlineEdittextRoundness))) {

        TextField(
            value = text,
            onValueChange = onTextChanged,
            placeholder = { Text(hint, color = systemGrayText) },
            modifier = Modifier.fillMaxWidth().padding(size2dp),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, textColor = MaterialTheme.colors.onSurface)
        )
    }

}

@Composable
fun OutLineEdittextLogin(modifier: Modifier = Modifier, hint:String, text:String, backgroundColor : Color = Color.Transparent,stroke: BorderStroke, onTextChanged:(String) -> Unit){

    Box(modifier = modifier.fillMaxWidth().background(backgroundColor, shape = RoundedCornerShape(outlineEdittextRoundness))) {

        OutlinedTextField(
            value = text,
            onValueChange = onTextChanged,
            placeholder = { Text(hint, color = systemGrayText) },
            modifier = Modifier.fillMaxWidth().padding(size2dp),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = transGray, focusedIndicatorColor =  MaterialTheme.colors.primaryVariant, unfocusedIndicatorColor = Color.Transparent, textColor = MaterialTheme.colors.onSurface)
        )
    }

}

@Composable
fun OutLineEdittextPassword(modifier: Modifier = Modifier, hint:String, text:String, backgroundColor : Color = Color.Transparent,stroke: BorderStroke, onTextChanged:(String) -> Unit){

    Box(modifier = modifier.fillMaxWidth().background(backgroundColor, shape = RoundedCornerShape(outlineEdittextRoundness))) {

        OutlinedTextField(
            value = text,
            onValueChange = onTextChanged,
            placeholder = { Text(hint, color = systemGrayText) },
            modifier = Modifier.fillMaxWidth().padding(size2dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = transGray, focusedIndicatorColor =  MaterialTheme.colors.primaryVariant, unfocusedIndicatorColor = Color.Transparent, textColor = MaterialTheme.colors.onSurface)
        )
    }

}
