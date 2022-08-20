package com.flepper.therapeutic.android.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.LocalAbsoluteElevation
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.flepper.therapeutic.android.presentation.theme.*
import com.flepper.therapeutic.android.presentation.widgets.RegularText


@Composable
fun HomeBottomNav(items: List<BottomScreen>,onClick:(BottomScreen) -> Unit) {

    var currentIndex by remember {
        mutableStateOf(0)
    }

    Surface(elevation = elevation16) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(bottomNavBackground(isSystemInDarkTheme())),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items.forEachIndexed { index, bottomScreen ->
                BottomNavItem(
                    item = bottomScreen,
                    index = index,
                    isSelected = index == currentIndex,
                    this
                ) { selectedIndex ->
                    currentIndex = selectedIndex
                    onClick(items[currentIndex])
                }
            }

        }
    }

}

@Composable
fun BottomNavItem(
    item: BottomScreen,
    index: Int,
    isSelected: Boolean,
    rowScope: RowScope,
    setIndex: (Int) -> Unit
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    rowScope.apply {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacing4dp),
            modifier = Modifier
                .weight(1f)
                .clickable(
                    indication = rememberRipple(bounded = true),
                    interactionSource = interactionSource
                ) { setIndex(index) },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.padding(bottom = spacing3dp)) {
                Box(
                    modifier = Modifier
                        .height(size1dot5dp)
                        .background(color = if (!isSelected) Color.Transparent else MaterialTheme.colors.primary)
                        .width(width24dp)
                )
            }

            Image(
                painter = painterResource(id = item.icon),
                contentDescription = "",
                Modifier.size(
                    size24dp
                ),
                colorFilter = if (index == 0 && isSelected) ColorFilter.tint(MaterialTheme.colors.primary) else null,
                contentScale = ContentScale.Fit
            )
            RegularText(
                text = item.route,
                color = if (!isSelected) MaterialTheme.colors.onSurface else MaterialTheme.colors.primary
            )

        }
    }
}