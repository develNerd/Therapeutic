package com.flepper.therapeutic.android.presentation.intro

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.flepper.therapeutic.android.R
import com.flepper.therapeutic.android.data.eutiGoals
import com.flepper.therapeutic.android.presentation.theme.*
import com.flepper.therapeutic.android.presentation.widgets.LargeBoldText
import com.flepper.therapeutic.android.presentation.widgets.RegularText
import com.flepper.therapeutic.android.presentation.widgets.RoundedCornerButton
import com.flepper.therapeutic.android.util.progressBarAnimationOffset
import com.flepper.therapeutic.android.util.sliderChangeTime
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SecondOnBoardingScreen(onBeginClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = mediumPadding, vertical = largePadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(
                largePadding
            ), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LargeBoldText(
                text = stringResource(id = R.string.app_name),
                color = MaterialTheme.colors.primary
            )

            LargeBoldText(text = stringResource(id = R.string.meet_euti))

            Image(
                painter = painterResource(id = R.drawable.img_euti),
                contentDescription = stringResource(id = R.string.euti),
                modifier = Modifier.size(size56dp)
            )

            RegularText(
                text = stringResource(id = R.string.hi_euti),
                textAlign = TextAlign.Center,
                size = large_bold_text_size
            )
            RegularText(
                text = stringResource(id = R.string.euti_help),
                textAlign = TextAlign.Center
            )
            RegularText(
                text = stringResource(id = R.string.euti_can),
                size = textSize12sp, textAlign = TextAlign.Center
            )

            val pagerState = rememberPagerState()
            var progress by remember {
                mutableStateOf(0F)
            }

            val animateProgress by animateFloatAsState(targetValue = progress, animationSpec = spring(stiffness = Spring.StiffnessMedium, visibilityThreshold = 0.002f))

            var maker by remember {
                mutableStateOf(100)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colors.onSecondary,
                        shape = RoundedCornerShape(normal_corner_radius)
                    )
            ) {

                HorizontalPager(
                    count = eutiGoals.size,
                    modifier = Modifier.fillMaxWidth(),
                    state = pagerState
                ) { page ->
                    val eutiGoal = eutiGoals[page]
                    EutiHelpItem(id = eutiGoal.icon, eutiGoal.description)

                }


                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = spacing32dp), activeColor = MaterialTheme.colors.onSurface
                )

                LinearProgressIndicator(
                    progress = animateProgress,
                    modifier = Modifier.fillMaxWidth()
                )

            }

            LaunchedEffect(key1 = pagerState.currentPage, block = {
                for (i in 0 until maker){
                    delay(sliderChangeTime/100)
                    if (progress < 100f){
                        progress += (0.01f + progressBarAnimationOffset.toFloat())
                    }
                }
            })


            when (pagerState.currentPage) {
                0 -> {

                    LaunchedEffect(key1 = pagerState.currentPage, block = {
                        progress = 0.0F
                        delay(sliderChangeTime)
                        pagerState.scrollToPage(1)
                    })

                }
                1 -> {

                    LaunchedEffect(key1 = true, block = {
                        progress = 0.0F
                        delay(sliderChangeTime)
                        pagerState.scrollToPage(2)
                    })
                }
                else -> {

                    LaunchedEffect(key1 = true, block = {
                        progress = 0.0F
                        delay(sliderChangeTime)
                        pagerState.scrollToPage(0)
                    })
                }
            }


        }

        RoundedCornerButton(
            text = stringResource(id = R.string.lets_start),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = largePadding)
                .fillMaxWidth()
        ) {
            onBeginClicked()
        }

    }
}

@Composable
fun EutiHelpItem(id: Int, description: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(mediumPadding), verticalArrangement = Arrangement.spacedBy(
            largeXPadding
        ), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(60.dp)
                .padding(mediumPadding)
                .background(color = lightGreenTrans64, RoundedCornerShape(100)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier.size(
                    size24dp
                )
            )
        }


        RegularText(
            text = description,
            textAlign = TextAlign.Center,
            size = large_bold_text_size
        )

        Spacer(modifier = Modifier.size(smallPadding))

    }

}