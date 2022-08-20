package com.flepper.therapeutic.android.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.flepper.therapeutic.android.R
import com.flepper.therapeutic.android.presentation.theme.*
import com.flepper.therapeutic.android.presentation.widgets.MediumTextBold
import com.flepper.therapeutic.android.presentation.widgets.RegularText
import com.flepper.therapeutic.android.presentation.widgets.RoundedCornerButton
import com.flepper.therapeutic.android.presentation.widgets.RoundedCornerButtonWithIcon
import com.flepper.therapeutic.android.util.resolveAvatarUrl
import com.flepper.therapeutic.data.models.Host
import com.flepper.therapeutic.data.models.WorldWideEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailedEventBottomSheet(worldWideEvent: WorldWideEvent,homeViewModel: HomeViewModel,modalBottomSheetState: ModalBottomSheetState) {


    Column(
        Modifier
            .fillMaxWidth()
            .padding(bottom = spacing12dp), verticalArrangement = Arrangement.spacedBy(spacing12dp)) {

        /** @Content Title*/
        BottomSheetTitle(title = worldWideEvent.hashTag)
        Column(modifier = Modifier.padding(vertical = spacing5dp, horizontal = spacing5dp), verticalArrangement = Arrangement.spacedBy(
            spacing4dp)) {
            val hosts = buildString {
                worldWideEvent.hosts?.forEachIndexed { index, host ->
                    append(host.name + if (index == worldWideEvent.hosts?.lastIndex) "" else ",")
                }
            }
            RegularText(text = stringResource(id = R.string.host_row, hosts))
            RegularText(text = stringResource(id = R.string.title_description,
                worldWideEvent.title, worldWideEvent.description
            ))

            /** @Content bottom sheet hosts row*/

            BottomSheetHostRow(listItems = worldWideEvent.hosts ?: emptyList())


            fun getColor() = try {
                val color = worldWideEvent.backGroundColor as Color
               if (color != lightRed) color else colorPrimaryDark
            }catch (e:Exception){
                colorPrimary
            }
            /** @Content Button Attend or Decline*/

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    spacing12dp
                )
            ) {
                var isAttending by remember {
                    mutableStateOf(worldWideEvent.isAttending)
                }

                RoundedCornerButton(
                    text = stringResource(id = if (!isAttending) R.string.attend else R.string.decline),
                    textPadding = 0.dp,
                    modifier = Modifier.width(
                        width200dp
                    ), backgroundColor = if (!isAttending) getColor() else lightRed
                ) {
                    isAttending = !isAttending
                    worldWideEvent.isAttending = isAttending
                    homeViewModel.saveEvent(worldWideEvent)
                }
                if (isAttending){
                    RegularText(text = stringResource(id = R.string.attending), color = colorPrimaryDark)
                }
            }
        }


    }
}

@Composable
fun BottomSheetTitle(title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                transGray,
                RoundedCornerShape(
                    topStart = bottomSheetRoundnessDp,
                    topEnd = bottomSheetRoundnessDp
                )
            )
            .padding(vertical = spacing4dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            spacing12dp
        )
    ) {
        BottomSheetGestureView()

        MediumTextBold(
            text = title,
            modifier = Modifier.padding(vertical = spacing12dp),
            textSize = large_bold_text_size
        )
    }
}

@Composable
fun BottomSheetGestureView(){
    Box(
        modifier = Modifier
            .width(bottomSheetGestureWidth)
            .height(bottomSheetGestureHeight)
            .background(color = grayBackground, RoundedCornerShape(100))
    )
}

@Composable
fun BottomSheetHostRow(listItems: List<Host>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            spacing12dp
        )
    ) {
        CareTeamItemRow(listItems = listItems)
        Box(
            modifier = Modifier
                .background(
                    colorPrimary,
                    shape = RoundedCornerShape(5)
                ),
            contentAlignment = Alignment.Center
        ) {
            RegularText(
                text = stringResource(id = R.string.hosts),
                modifier = Modifier.padding(horizontal = spacing12dp, vertical = 1.dp),
                color = Color.White,
                size = 10.sp
            )
        }
    }
}

@Composable
fun CareTeamItemRow(listItems: List<Host>) {
    var nextPadding by remember {
        mutableStateOf(20)
    }



    BoxWithConstraints(
        modifier = Modifier
            .padding(smallPadding), contentAlignment = Alignment.CenterStart
    ) {
        val context = LocalContext.current
        listItems.forEachIndexed { indexW, host ->
            val image by remember {
                mutableStateOf( resolveAvatarUrl(
                    context,
                    host.avatar
                ))
            }
            val padding by remember {
                mutableStateOf(20 + indexW * 20)
            }
            Row(modifier = Modifier.padding(start = if (indexW == 0) 0.dp else padding.dp - 20.dp)) {
                AsyncImage(
                    model = resolveAvatarUrl(
                        LocalContext.current,
                        host.avatar
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(avatarSie, avatarSie)
                        .background(transGray, RoundedCornerShape(100))
                        .clip(RoundedCornerShape(100)),
                )
            }

        }

    }


}