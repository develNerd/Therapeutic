package com.flepper.therapeutic.android.presentation.home

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import coil.compose.AsyncImage
import com.bumptech.glide.Glide
import com.flepper.therapeutic.android.R
import com.flepper.therapeutic.android.databinding.BotTypingLayoutBinding
import com.flepper.therapeutic.android.databinding.EqualiserLayoutBinding
import com.flepper.therapeutic.android.databinding.EqualiserSmallLayoutBinding
import com.flepper.therapeutic.android.presentation.theme.*
import com.flepper.therapeutic.android.presentation.widgets.MediumTextBold
import com.flepper.therapeutic.android.presentation.widgets.RegularText
import com.flepper.therapeutic.android.util.*
import com.flepper.therapeutic.data.models.FeaturedContent
import com.flepper.therapeutic.data.models.WorldWideEvent
import com.flepper.therapeutic.data.models.appointments.booking.BookAppointmentResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO ("Restructure Compose Functions i.e Into Respective Classes")
@Composable
fun HomeScreen(homeViewModel: HomeViewModel, onFeaturedItemClicked: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(mediumPadding), verticalArrangement = Arrangement.spacedBy(spacing12dp)
        ) {
            Row(Modifier.fillMaxWidth()) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_tr),
                    contentDescription = "",
                    tint = MaterialTheme.colors.primary
                )
            }
            MediumTextBold(
                text = stringResource(
                    id = R.string.hello_user,
                    homeViewModel.appPreferences.anonUser?.userName ?: ""
                ), textSize = large_bold_text_size
            )

        }
        val localSessions by homeViewModel.localSession.collectAsState()

        if (localSessions.isNotEmpty()) {
            val localSession = localSessions.first()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = spacing3dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    spacing5dp
                )
            ) {
                ScheduleItemChip(
                    text = stringResource(id = R.string.upcoming) + " • " + localSession.startAt.fromApiTime()
                        .parseToMonthDayString() + " • " + localSession.startAt.fromApiTime()
                        .parseToHourMinuteString(),
                    baseColor = lightViolet,
                    icon = R.drawable.ic_schedule_session,
                    false,
                ) {
                    homeViewModel.setCurrentBottomSheetType(BottomSheetContentType.APPOINTMENT)
                    homeViewModel.refreshSessionSelection()
                }
            }
        }

        FeaturedContentRow(
            homeViewModel = homeViewModel,
            onFeaturedItemClicked = onFeaturedItemClicked
        )
        WorldWideEventsColumn(viewModel = homeViewModel)
    }
}


@Composable
fun FeaturedContentRow(homeViewModel: HomeViewModel, onFeaturedItemClicked: () -> Unit) {

    val featuredItems by homeViewModel.featuredContent.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(
            spacing16dp
        )
    ) {
        MediumTextBold(
            text = stringResource(id = R.string.featured_content),
            color = MaterialTheme.colors.primaryVariant,
            modifier = Modifier.padding(start = mediumPadding)
        )
        if (featuredItems.result != null) {
            Log.e("Result2", featuredItems.result.toString())
            GenericListViewRenderer(
                list = featuredItems.result!!,
                loadComplete = featuredItems.isLoaded
            ) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(spacing12dp)) {
                    itemsIndexed(featuredItems.result!!) { index, item: FeaturedContent ->
                        if (item == featuredItems.result!!.first()) {
                            Spacer(modifier = Modifier.size(mediumPadding))
                        }
                        FeaturedContentItem(item = item, index) { currentIndex ->
                            onFeaturedItemClicked()
                            homeViewModel.setCurrentFeaturedContent(featuredItems.result!![currentIndex])
                        }
                        if (item == featuredItems.result!!.last()) {
                            Spacer(modifier = Modifier.size(mediumPadding))
                        }
                    }


                }
            }
        }

    }
}


/** @FeaturedContentColumn*/

@Composable
fun FeaturedContentColumn(homeViewModel: HomeViewModel) {

    val featuredItems by homeViewModel.featuredContent.collectAsState()
    val currentItem by homeViewModel.currentFeaturedContent.collectAsState()

    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .padding(vertical = smallPadding)
            .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(
            spacing16dp
        )
    ) {
        /** @Column author and title*/
        Column(
            verticalArrangement = Arrangement.spacedBy(spacing0dot5dp),
            modifier = Modifier.padding(horizontal = mediumPadding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    spacing4dp
                )
            ) {
                /** @Row Title */
                Row(
                    modifier = Modifier
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .padding(vertical = mediumPadding), contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_topic),
                            contentDescription = "",
                            modifier = Modifier.size(
                                size20dp
                            ), tint = paleGreen
                        )
                    }

                    MediumTextBold(
                        text = currentItem.first().description,
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier
                            .padding(start = mediumPadding, top = 1.5.dp),
                        textSize = headerTextSize
                    )
                }

                /** @Row Fav */
                Row(horizontalArrangement = Arrangement.spacedBy(smallPadding)) {
                    IconToggleButton(checked = false, onCheckedChange = {}) {
                        Icon(
                            painter = painterResource(id = R.drawable.thumb_up),
                            contentDescription = "",
                            tint = MaterialTheme.colors.onBackground,
                            modifier = Modifier.size(
                                size24dp
                            )
                        )
                    }
                    IconToggleButton(checked = false, onCheckedChange = {}) {
                        Icon(
                            painter = painterResource(id = R.drawable.thumb_down),
                            contentDescription = "",
                            tint = MaterialTheme.colors.onBackground,
                            modifier = Modifier.size(
                                size24dp
                            )
                        )
                    }
                }

            }

            Row {
                RegularText(text = stringResource(id = R.string.by_author, "Master Sri Arkashana"))
            }
        }

        if (featuredItems.result != null) {
            Log.e("Result2", featuredItems.result.toString())
            GenericListViewRenderer(
                list = featuredItems.result!!,
                loadComplete = featuredItems.isLoaded
            ) {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(spacing12dp)) {
                    items(featuredItems.result!!) { item: FeaturedContent ->
                        Line()
                        FeaturedContentColumnItem(item = item)
                        if (item == featuredItems.result!!.last()) {
                            Spacer(modifier = Modifier.size(mediumPadding))
                            Line()
                        }
                    }


                }
            }
        }

    }
}

@Composable
fun Line() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 0.5.dp)
            .background(
                textGray
            )
    )
}

@Composable
fun FeaturedContentItem(item: FeaturedContent, index: Int, onFeaturedItemClicked: (Int) -> Unit) {
    Box(modifier = Modifier
        .size(width = width200dp, height = height100dp)
        .clickable {
            onFeaturedItemClicked(index)
        }) {
        AsyncImage(
            model = resolveImageUrl(LocalContext.current, item.thumbNail),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(size10dp))
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = transBlack, shape = RoundedCornerShape(
                        size10dp
                    )
                )
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(spacing4dp), modifier = Modifier
                .align(
                    Alignment.BottomStart
                )
                .padding(smallPadding)
        ) {
            Box(modifier = Modifier.background(transBlackDark, shape = RoundedCornerShape(100))) {
                RegularText(
                    text = item.duration,
                    size = textSize11sp,
                    modifier = Modifier.padding(vertical = spacing3dp, horizontal = spacing5dp),
                    color = textWhite
                )
            }
            Box(modifier = Modifier.background(transBlackDark, shape = RoundedCornerShape(100))) {
                RegularText(
                    text = item.description,
                    size = textSize11sp,
                    modifier = Modifier.padding(vertical = spacing3dp, horizontal = spacing5dp),
                    color = textWhite
                )
            }
        }
    }
}

@Composable
fun FeaturedContentColumnItem(item: FeaturedContent) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(), verticalArrangement = Arrangement.spacedBy(
            mediumPadding
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height100dp)
                .padding(horizontal = mediumPadding, vertical = smallPadding)
        )
        {


            AsyncImage(
                model = resolveImageUrl(LocalContext.current, item.thumbNail),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(size10dp))
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = transBlack, RoundedCornerShape(size10dp)
                    )
            )


        }
        Column(
            verticalArrangement = Arrangement.spacedBy(spacing2dp),
            modifier = Modifier.padding(horizontal = mediumPadding)
        ) {
            Row(
                modifier = Modifier, verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.ic_topic),
                    contentDescription = "",
                    modifier = Modifier.size(
                        size15dp
                    ), tint = lightViolet
                )

                MediumTextBold(
                    text = item.description,
                    modifier = Modifier.padding(vertical = spacing3dp, horizontal = spacing5dp),
                    color = MaterialTheme.colors.onBackground
                )

                RegularText(
                    text = " - " + "Master Sri Arkashana",
                    modifier = Modifier.padding(vertical = spacing3dp, horizontal = spacing5dp),
                    color = MaterialTheme.colors.onBackground
                )
            }
            Box(
                modifier = Modifier.background(
                    transBlackDark,
                    shape = RoundedCornerShape(100)
                )
            ) {
                RegularText(
                    text = item.duration,
                    size = textSize11sp,
                    modifier = Modifier.padding(vertical = spacing3dp, horizontal = smallPadding),
                    color = textWhite
                )
            }
        }
    }
}

@Composable
fun WorldWideEventsColumn(viewModel: HomeViewModel) {

    val events by viewModel.events.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = mediumPadding, vertical = mediumPadding),
        verticalArrangement = Arrangement.spacedBy(
            smallPadding
        )
    ) {
        MediumTextBold(
            text = stringResource(id = R.string.world_events),
            color = MaterialTheme.colors.primaryVariant
        )
        GenericListViewRenderer(list = events.result, loadComplete = events.isLoaded) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(spacing12dp)) {

                itemsIndexed(events.result!!) { index, item: WorldWideEvent ->

                    val isFirst = item == events.result!!.first()

                    if (isFirst) {
                        Spacer(modifier = Modifier.size(smallPadding))
                    }
                    val backGroundColor =
                        if (isFirst || (index % eventColors.size + 1) == 0) MaterialTheme.colors.background else item.backGroundColor as Color
                    WorldWideEventsItem(
                        worldWideEvent = item,
                        backGroundColor,
                        index,
                        isBackgroundThemed = backGroundColor == MaterialTheme.colors.background
                    ) { currentEvent ->
                        viewModel.getEvent(currentEvent.id, worldWideEvent = currentEvent)
                        viewModel.refreshEventSelection()
                        viewModel.setCurrentBottomSheetType(BottomSheetContentType.EVENT)
                    }

                    if (item == events.result!!.last()) {
                        Spacer(modifier = Modifier.size(bottomNavHeight))
                    }
                }


            }
        }
    }
}

var currentChipIndex = ChipIndex()

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorldWideEventsItem(
    worldWideEvent: WorldWideEvent,
    backGroundColor: Color,
    index: Int,
    isBackgroundThemed: Boolean = false,
    onClicked: (WorldWideEvent) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(event_roundness),
        elevation = elevation16,
        color = backGroundColor,
        modifier = Modifier.wrapContentHeight(), onClick = {
            onClicked(worldWideEvent)
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(mediumPadding)
        ) {
            /** @Row Top Chips */

            Row(
                modifier = Modifier
                    .wrapContentHeight()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(
                    mediumPadding
                )
            ) {
                Spacer(modifier = Modifier.padding(start = 1.0.dp))
                worldWideEvent.apply {
                    val textIsOngoing = if (isOngoing) stringResource(id = R.string.ongoing) else ""

                    val eventChips = mapOf<String, Int>(
                        textIsOngoing to EQUALISER_DRAWABLE_INT,
                        startTime!!.parseToMonthDayString() to R.drawable.ic_calendar,
                        startTime!!.parseToHourMinuteString() to R.drawable.ic_clock,
                    )


                    eventChips.forEach { (text, icon) ->
                        if (text.isNotEmpty()) {

                            EventChipItem(
                                text = text,
                                baseColor = backGroundColor,
                                icon = icon,
                                isBackgroundThemed,
                                worldWideEvent.isOngoing
                            )

                        }
                    }
                    Spacer(modifier = Modifier.size(smallPadding))
                }

            }

            /** @Row Hosts */

            Row(
                modifier = Modifier.horizontalPadding(mediumPadding),
                horizontalArrangement = Arrangement.spacedBy(
                    mediumPadding
                ), verticalAlignment = Alignment.CenterVertically
            ) {

                val hosts = worldWideEvent.hosts

                val host = hosts?.get(index % 3)
                AsyncImage(
                    model = resolveAvatarUrl(
                        LocalContext.current,
                        host?.avatar ?: ""
                    ),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .background(transGray, RoundedCornerShape(100))
                        .size(size30dp)
                        .clip(RoundedCornerShape(completeRoundness))
                )

                RegularText(
                    text = host?.name,
                    size = medium_bold_text_size,
                    color = if (isBackgroundThemed) MaterialTheme.colors.onBackground else systemGray
                )

                Box(
                    modifier = Modifier
                        .background(
                            if (isBackgroundThemed) transGray else transWhite,
                            shape = RoundedCornerShape(5)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    MediumTextBold(
                        text = stringResource(id = R.string.main_host),
                        modifier = Modifier.padding(horizontal = spacing4dp, vertical = spacing3dp),
                        textSize = regular_bold_text_size,
                        color = if (isBackgroundThemed) MaterialTheme.colors.onBackground else systemGray
                    )
                }


            }
            /** @Row Category */

            Row(
                modifier = Modifier
                    .padding(horizontal = mediumPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .padding(vertical = mediumPadding), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_topic),
                        contentDescription = "",
                        modifier = Modifier.size(
                            size20dp
                        ), tint = if (isBackgroundThemed) lightRed else systemGray
                    )
                }

                RegularText(
                    text = stringResource(
                        id = R.string.title_description,
                        worldWideEvent.category,
                        worldWideEvent.description
                    ),
                    color = if (isBackgroundThemed) MaterialTheme.colors.onSurface else systemGray,
                    modifier = Modifier
                        .padding(start = mediumPadding, top = 1.5.dp),
                    size = regular_bold_text_size
                )
            }

            /** @Row HashTag */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (isBackgroundThemed) transGray else transWhite)
            ) {
                MediumTextBold(
                    text = worldWideEvent.hashTag,
                    textSize = large_bold_text_size,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = smallPadding, vertical = mediumPadding),
                    color = if (isBackgroundThemed) MaterialTheme.colors.onBackground else systemGray,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    modifier = Modifier
                        .weight(0.5f)
                        .horizontalPadding(smallPadding)
                ) {

                }
            }

        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppointmentItem(
    appointment: BookAppointmentResponse,
    backGroundColor: Color = lightViolet,
    index: Int,
    isBackgroundThemed: Boolean = false,
    onClicked: (BookAppointmentResponse) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(event_roundness),
        elevation = elevation16,
        color = backGroundColor,
        modifier = Modifier
            .wrapContentHeight()
            .padding(horizontal = eutiChatHorizontalSpacing), onClick = {
            onClicked(appointment)
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(mediumPadding)
        ) {
            /** @Row Top Chips */

            Row(
                modifier = Modifier
                    .wrapContentHeight()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(
                    mediumPadding
                )
            ) {
                Spacer(modifier = Modifier.padding(start = 1.0.dp))
                val upComing = stringResource(id = R.string.upcoming)
                val location = stringResource(id = R.string.therapeutic_h1)
                appointment.apply {


                    val eventChips = mapOf<String, Int>(
                        startAt.fromApiTime()
                            .parseToMonthDayString() to R.drawable.ic_calendar,
                        startAt.fromApiTime()
                            .parseToHourMinuteString() to R.drawable.ic_clock,
                        location to R.drawable.ic_baseline_location_on_24
                    )


                    eventChips.forEach { (text, icon) ->
                        if (text.isNotEmpty()) {

                            EventChipItem(
                                text = text,
                                baseColor = backGroundColor,
                                icon = icon,
                                isBackgroundThemed,
                            )

                        }
                    }
                    Spacer(modifier = Modifier.size(smallPadding))
                }

            }


            /** @Row Category */

            Row(
                modifier = Modifier
                    .padding(horizontal = mediumPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .padding(vertical = mediumPadding), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_topic),
                        contentDescription = "",
                        modifier = Modifier.size(
                            size20dp
                        ), tint = if (isBackgroundThemed) lightRed else systemGray
                    )
                }

                RegularText(
                    text = stringResource(id = R.string.therapy),
                    color = if (isBackgroundThemed) MaterialTheme.colors.onSurface else systemGray,
                    modifier = Modifier
                        .padding(start = mediumPadding, top = 1.5.dp),
                    size = regular_bold_text_size
                )
            }

            /** @Row HashTag */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (isBackgroundThemed) transGray else transWhite)
            ) {
                MediumTextBold(
                    text = stringResource(id = R.string.therapy_session_with_therapeutic),
                    textSize = regular_bold_text_size,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = smallPadding, vertical = mediumPadding),
                    color = if (isBackgroundThemed) MaterialTheme.colors.onBackground else systemGray,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    modifier = Modifier
                        .weight(0.5f)
                        .horizontalPadding(smallPadding)
                ) {

                }
            }

        }
    }
}

fun Modifier.horizontalPadding(value: Dp): Modifier {
    return this.padding(horizontal = value)
}


data class ChipIndex(var currentChipIndex: Int = 0)

@Composable
fun EventChipItem(
    text: String,
    baseColor: Color,
    @DrawableRes icon: Int,
    isBackgroundThemed: Boolean = false,
    isOngoing: Boolean = false
) {

    val color = when (icon) {
        EQUALISER_DRAWABLE_INT -> _backGroundColors[0]
        R.drawable.ic_calendar -> _backGroundColors[1]
        else -> _backGroundColors[2]
    }
    val chipBaseColor = if (isBackgroundThemed) color else systemGray
    Row(
        modifier = Modifier
            .padding(vertical = smallPadding)
            .border(
                border = BorderStroke(borderStroke1dot8dp, chipBaseColor),
                shape = RoundedCornerShape(100)
            )
    ) {
        Box(
            modifier = Modifier
                .background(
                    Color(
                        chipBaseColor.red,
                        chipBaseColor.green,
                        chipBaseColor.blue,
                        alpha = 0.2f
                    ), RoundedCornerShape(100)
                )
                .padding(mediumPadding), contentAlignment = Alignment.Center
        ) {
            if (isOngoing && icon == EQUALISER_DRAWABLE_INT) {
                AndroidViewBinding(EqualiserSmallLayoutBinding::inflate) {
                    imageTyping.apply {
                        Glide.with(context)
                            .load(R.drawable.ic_event_ongoing)
                            .into(this)
                    }
                }
            } else {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "",
                    modifier = Modifier.size(
                        size15dp
                    ), tint = chipBaseColor
                )

            }

        }

        RegularText(
            text = text,
            color = chipBaseColor,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = mediumPadding),
            size = textSize11sp
        )
    }

}

@Composable
fun ScheduleItemChip(
    text: String,
    baseColor: Color,
    @DrawableRes icon: Int,
    isBackgroundThemed: Boolean = false,
    onClicked: () -> Unit
) {

    val color = MaterialTheme.colors.primaryVariant
    val chipBaseColor = if (isBackgroundThemed) color else systemGray
    Row(
        modifier = Modifier
            .padding(vertical = smallPadding)
            .border(
                border = BorderStroke(borderStroke1dot8dp, chipBaseColor),
                shape = RoundedCornerShape(100)
            )
            .clickable {
                onClicked()
            }.clipToBounds()
    ) {
        Box(
            modifier = Modifier
                .background(
                    Color(
                        chipBaseColor.red,
                        chipBaseColor.green,
                        chipBaseColor.blue,
                        alpha = 0.2f
                    ), RoundedCornerShape(100)
                )
                .padding(mediumPadding), contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "",
                modifier = Modifier.size(
                    size15dp
                ), tint = chipBaseColor
            )
        }

        RegularText(
            text = text,
            color = chipBaseColor,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = mediumPadding),
            size = textSize11sp
        )
    }

}

@Composable
fun PodCastView(
    text: String,
    baseColor: Color,
    @DrawableRes icon: Int,
    isBackgroundThemed: Boolean = false,
    onClicked: () -> Unit
) {

    val color = MaterialTheme.colors.primaryVariant
    val chipBaseColor = if (isBackgroundThemed) color else systemGray
    var isPlaying by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = smallPadding, horizontal = eutiChatHorizontalSpacing)
            .border(
                border = BorderStroke(borderStroke1dot8dp, chipBaseColor),
                shape = RoundedCornerShape(100)
            )
            .clickable {
                onClicked()
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(
                    Color(
                        chipBaseColor.red,
                        chipBaseColor.green,
                        chipBaseColor.blue,
                        alpha = 0.2f
                    ), RoundedCornerShape(100)
                )
                .padding(mediumPadding), contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = if (isPlaying) R.drawable.ic_baseline_pause_24 else R.drawable.ic_baseline_play_arrow_24),
                contentDescription = "",
                modifier = Modifier.size(
                    size15dp
                ), tint = chipBaseColor
            )
        }

        var isRestart by remember {
            mutableStateOf(true)
        }
        var scrollState = rememberScrollState(0)
        var scrollValue by remember {
            mutableStateOf(0)
        }
        val scrollAnimate by animateIntAsState(targetValue = scrollValue, animationSpec = tween(durationMillis = 1, easing = FastOutSlowInEasing))
        LaunchedEffect(key1 = isRestart, block = {
            while (scrollState.value < scrollState.maxValue){
                scrollValue = scrollState.value + 8
                scrollState.animateScrollTo(scrollAnimate,animationSpec = tween(durationMillis = 1, easing = FastOutSlowInEasing, delayMillis = 1))
            }
            if (scrollState.value >= scrollState.maxValue){
                scrollValue = 0
                scrollState.animateScrollTo(scrollAnimate,animationSpec = tween(durationMillis = 1, easing = FastOutSlowInEasing))
                isRestart = !isRestart
            }
        })

        Row(modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)) {
            RegularText(
                text = text,
                color = chipBaseColor,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = mediumPadding),
                size = textSize11sp
            )
        }

    }

}