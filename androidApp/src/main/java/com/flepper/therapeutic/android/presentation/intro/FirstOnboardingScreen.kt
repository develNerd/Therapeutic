package com.flepper.therapeutic.android.presentation.intro

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.flepper.therapeutic.android.R
import com.flepper.therapeutic.android.presentation.theme.*
import com.flepper.therapeutic.android.presentation.widgets.*

@Composable
fun FirstOnBoardingScreen(defUserName:String = "",onContinueClicked:(String) -> Unit) {
    var isTextFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val density = LocalDensity.current
    var userName by remember {
        mutableStateOf(defUserName)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = mediumPadding, vertical = largePadding)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(
                largePadding
            ), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LargeBoldText(
                text = stringResource(id = R.string.app_name),
                color = MaterialTheme.colors.primary
            )
            AnimatedVisibility(visible = !isTextFocused, enter = fadeIn(), exit = fadeOut()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = largePadding),
                    verticalArrangement = Arrangement.spacedBy(
                        size30dp
                    ), horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    LargeBoldText(text = stringResource(id = R.string.lets_begin_excl))
                    RegularText(text = stringResource(id = R.string.first_onboarding_desc), textAlign = TextAlign.Center)
                    RegularText(
                        text = stringResource(id = R.string.name_to_begin_with),
                        size = textSize12sp
                    )
                }
            }



            OutLineEdittext(
                hint = stringResource(id = R.string.your_name),
                stroke = BorderStroke(
                    size05dp, gray
                ),
                text = userName,
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        if (!isTextFocused && focusState.isFocused) {
                            isTextFocused = true
                        } else if (isTextFocused && !focusState.isFocused) {
                            isTextFocused = false
                        }
                    }
            ) { name ->
                userName = name
            }

        }


        RoundedCornerButton(
            text = stringResource(id = R.string.continue_button),
            isEnabled = userName.isNotEmpty(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = largePadding)
                .fillMaxWidth()
        ) {
            onContinueClicked(userName)
        }
    }

}