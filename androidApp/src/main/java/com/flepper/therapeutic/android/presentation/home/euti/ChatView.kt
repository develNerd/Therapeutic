package com.flepper.therapeutic.android.presentation.home.euti

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.bumptech.glide.Glide
import com.flepper.therapeutic.android.R
import com.flepper.therapeutic.android.databinding.BotTypingLayoutBinding
import com.flepper.therapeutic.android.presentation.home.*
import com.flepper.therapeutic.android.presentation.theme.*
import com.flepper.therapeutic.android.presentation.widgets.MediumTextBold
import com.flepper.therapeutic.data.models.WorldWideEvent
import com.flepper.therapeutic.data.models.appointments.booking.BookAppointmentResponse
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import kotlin.math.absoluteValue

sealed class EutiChatType {
    class Euti(
        val text: String,
        var isHead: Boolean,
        val customContent: @Composable () -> Unit = {}
    ) : EutiChatType()

    class User(val text: String, var isHead: Boolean) : EutiChatType()
    class Content(
        val id: String,
        val content: List<WorldWideEvent> = emptyList(),
        val localSessions: List<BookAppointmentResponse> = emptyList(),
        val sheetContentType: SheetContentType
    ) : EutiChatType()

    object Default
}

@Composable
fun ChatView(
    modifier: Modifier = Modifier,
    eutiChats: List<EutiChatType>,
    paddingBottom: Dp = 200.dp,
    eutiViewModel: EutiViewModel,
    homeViewModel: HomeViewModel
) {
    val isChatAdded by eutiViewModel.isChatAdded.collectAsState()
    val isChatLoading by eutiViewModel.isChatLoading.collectAsState()
    val scrollState = rememberScrollState()
    val ottoChatReplies = eutiChats.toMutableList()



    LaunchedEffect(key1 = isChatAdded) {
        scrollState.animateScrollTo(Int.MAX_VALUE)
        eutiViewModel.setIsChatAdded(false)
    }



    Column(
        modifier = Modifier
            .padding(bottom = paddingBottom)
            .verticalScroll(scrollState)
            .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(
            spacing6dp
        )
    ) {


        ottoChatReplies.forEachIndexed { index, chat ->
            var show by remember {
                mutableStateOf(false)
            }

            Box {
                when (chat) {
                    is EutiChatType.Euti -> {
                        EutiBotView(chat.text, chat.isHead)
                    }
                    is EutiChatType.User -> {
                        UserChatVIew(chat.text, chat.isHead)
                    }
                    is EutiChatType.Content -> {
                        when (chat.sheetContentType) {
                            SheetContentType.ONGOING_EVENTS -> {
                                SheetContentTypeOngoingEvents(
                                    ongoingEvents = chat.content,
                                    homeViewModel
                                )
                            }
                            SheetContentType.UPCOMING_EVENTS -> {
                                Box(contentAlignment = Alignment.CenterStart) {
                                    SheetContentTypeOngoingEvents(
                                        ongoingEvents = chat.content,
                                        homeViewModel
                                    )

                                }
                            }
                            SheetContentType.SCHEDULE_SESSION -> {
                                if (chat.localSessions.isNotEmpty()) {
                                    Box(contentAlignment = Alignment.CenterStart) {
                                        AppointmentItem(
                                            appointment = chat.localSessions.first(),
                                            index = 0,
                                            onClicked = {
                                                homeViewModel.setCurrentBottomSheetType(BottomSheetContentType.APPOINTMENT)
                                                homeViewModel.refreshSessionSelection()
                                            })
                                    }
                                }

                            }
                            SheetContentType.LISTEN_TO_PODCASTS -> {
                                PodCastView(text = stringResource(id = R.string.response_to_found_events), baseColor = lightViolet, icon = R.drawable.ic_baseline_play_arrow_24) {

                                }
                            }
                            SheetContentType.DEFAULT -> {

                            }
                        }
                    }
                }
            }
        }
        if (isChatLoading) {
            EutiBotView("", true)
        }
    }

}


data class ContentItemValues<T>(val id: T)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SheetContentTypeOngoingEvents(
    ongoingEvents: List<WorldWideEvent>,
    homeViewModel: HomeViewModel
) {

    HorizontalPager(count = ongoingEvents.size) { page ->
        Box(
            Modifier
                .padding(horizontal = eutiChatHorizontalSpacing, vertical = spacing2dp)
                .wrapContentSize()
                .graphicsLayer {
                    // Calculate the absolute offset for the current page from the
                    // scroll position. We use the absolute value which allows us to mirror
                    // any effects for both directions
                    val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                    // We animate the scaleX + scaleY, between 85% and 100%
                    lerp(
                        start = 0.85f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    ).also { scale ->
                        scaleX = scale
                        scaleY = scale
                    }

                    // We animate the alpha, between 50% and 100%
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }, contentAlignment = Alignment.CenterStart

        ) {
            // Card content
            WorldWideEventsItem(
                worldWideEvent = ongoingEvents[page],
                backGroundColor = ongoingEvents[page].backGroundColor as Color,
                index = page,
                onClicked = { selected ->
                    homeViewModel.setSelectedEvent(selected)
                    homeViewModel.refreshEventSelection()
                    homeViewModel.setCurrentBottomSheetType(BottomSheetContentType.EVENT)

                })

            if (ongoingEvents.first().id == ongoingEvents[page].id && !ongoingEvents[page].isOngoing) {
                AndroidViewBinding(
                    BotTypingLayoutBinding::inflate,
                    modifier = Modifier.padding(start = mediumPadding)
                ) {
                    imageTyping.apply {
                        Glide.with(context)
                            .load(R.drawable.ic_scrolling_left)
                            .into(this)
                    }
                }
            }


        }
    }
}

@Composable
fun EutiBotView(text: String, isHead: Boolean = false) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = startChatSpacing, end = endChatSpacing)
    ) {
        Column(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalArrangement = Arrangement.spacedBy(smallPadding)
        ) {

            if (isHead) {
                Image(
                    painter = painterResource(id = R.drawable.img_euti),
                    contentDescription = "",
                    modifier = Modifier.size(size24dp)
                )
            }
            Box(
                modifier = Modifier.background(
                    color = eutiChatBackgroundColor(isSystemInDarkTheme()),
                    shape = RoundedCornerShape(
                        topStart = if (isHead) 0.dp else size10dp,
                        topEnd = size10dp,
                        bottomEnd = size10dp,
                        bottomStart = size10dp
                    )
                )
            ) {
                if (text != "") {
                    MediumTextBold(
                        text = text,
                        modifier = Modifier.padding(
                            mediumPadding
                        ),
                        color = if (text == stringResource(id = R.string.yay_booked)) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
                    )
                } else {
                    val context = LocalContext.current
                    Box(modifier = Modifier.padding(mediumPadding)) {
                        AndroidViewBinding(BotTypingLayoutBinding::inflate) {
                            imageTyping.apply {
                                Glide.with(context)
                                    .load(R.drawable.bot_typing)
                                    .into(this)
                            }
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun UserChatVIew(text: String, isHead: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = startChatSpacing, start = endChatSpacing)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .background(
                    color = contrastBlue,
                    shape = RoundedCornerShape(
                        topStart = size10dp,
                        topEnd = size10dp,
                        bottomEnd = if (isHead) 0.dp else size10dp,
                        bottomStart = size10dp
                    )
                )
        ) {
            MediumTextBold(
                text = text,
                modifier = Modifier.padding(
                    mediumPadding
                ),
                color = Color.White
            )
        }
    }

}
