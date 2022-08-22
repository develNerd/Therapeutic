package com.flepper.therapeutic.android.presentation.home.euti

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.flepper.therapeutic.android.R
import com.flepper.therapeutic.android.presentation.home.HomeViewModel
import com.flepper.therapeutic.android.presentation.theme.*
import com.flepper.therapeutic.android.presentation.widgets.MediumTextBold
import java.util.*


/** @MainSheet Content*/


@Composable
fun MainSheet(
    items: List<EutiMainSheetItem>,
    navController: NavController,
    eutiViewModel: EutiViewModel,
    homeViewModel: HomeViewModel,
    onFeaturedVideosCLick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = eutiChatHorizontalSpacing, vertical = mediumPadding),
        verticalArrangement = Arrangement.spacedBy(
            mediumPadding
        )
    ) {
        var optionId by remember {
            mutableStateOf(UUID.randomUUID().toString())
        }
        val coroutinesScope = rememberCoroutineScope()
        val context = LocalContext.current
        val localSession by homeViewModel.localSession.collectAsState()
        items.forEachIndexed { index, mainSheetItem ->
            MainSheetItem(item = mainSheetItem) { currentItem ->
                when (currentItem.type) {
                    SheetContentType.ONGOING_EVENTS -> {
                        val chat = EutiChatType.User(currentItem.name, false).apply {
                            this.isHead = eutiViewModel.checkHead(this)
                        }
                        eutiViewModel.addToReplies(EutiChatType.User(currentItem.name, chat.isHead))
                        eutiViewModel.setGenericTitle(currentItem.name)
                        navController.navigate(EutiScreens.GenericBottomView(eutiViewModel =  eutiViewModel).screenName)
                        optionId = "${UUID.randomUUID()}"
                        eutiViewModel.setOptionBottomId(optionId)
                        val eutiChat = EutiChatType.Content(
                            eutiViewModel.currentOptionID.value,
                            emptyList(), emptyList() ,SheetContentType.ONGOING_EVENTS
                        )
                        eutiViewModel.setIsChatLoading(true)
                        eutiViewModel.setCurrentEutiType(eutiChat)
                        eutiViewModel.getOnGoingEvents(true)

                    }
                    SheetContentType.UPCOMING_EVENTS -> {
                        val chat = EutiChatType.User(currentItem.name, false).apply {
                            this.isHead = eutiViewModel.checkHead(this)
                        }
                        eutiViewModel.addToReplies(EutiChatType.User(currentItem.name, chat.isHead))
                        eutiViewModel.setGenericTitle(currentItem.name)
                        navController.navigate(EutiScreens.GenericBottomView(eutiViewModel =  eutiViewModel).screenName)
                        optionId = "${UUID.randomUUID()}"
                        eutiViewModel.setOptionBottomId(optionId)
                        val eutiChat = EutiChatType.Content(
                            optionId,
                            emptyList(),
                            listOf(),
                            SheetContentType.UPCOMING_EVENTS
                        )
                        eutiViewModel.setIsChatLoading(true)
                        eutiViewModel.setCurrentEutiType(eutiChat)
                        eutiViewModel.getOnGoingEvents(false)

                    }
                    SheetContentType.SCHEDULE_SESSION -> {
                        if (localSession.isEmpty()){
                            val chat = EutiChatType.User(currentItem.name, false).apply {
                                this.isHead = eutiViewModel.checkHead(this)
                            }
                            eutiViewModel.addToReplies(EutiChatType.User(currentItem.name, chat.isHead))
                            if (eutiViewModel.appPreferences.signInUser == null)
                            {
                                val eutiChat = EutiChatType.Euti(context.getString(R.string.to_schedule_req), false).apply {
                                    this.isHead = eutiViewModel.checkHead(this)
                                }
                                eutiViewModel.addToReplies(eutiChat)
                                val eutiChat2 = EutiChatType.Euti(context.getString(R.string.to_schedule_req_sign_up), false).apply {
                                    this.isHead = eutiViewModel.checkHead(this)
                                }
                                eutiViewModel.addToReplies(eutiChat2)
                                navController.navigate(EutiScreens.ToSignUpOrSignInScreen(eutiViewModel).screenName)
                            }else
                            {

                                navController.navigate(EutiScreens.ScheduleSessionDateScreen(eutiViewModel).screenName)
                            }
                        }else{
                            val chat = EutiChatType.User(currentItem.name, false).apply {
                                this.isHead = eutiViewModel.checkHead(this)
                            }
                            val eutiChat = EutiChatType.Euti(context.getString(R.string.one_schdule), false).apply {
                                this.isHead = eutiViewModel.checkHead(this)
                            }
                            eutiViewModel.addToReplies(eutiChat)
                            eutiViewModel.addToReplies(EutiChatType.User(currentItem.name, chat.isHead))
                            eutiViewModel.addToReplies(EutiChatType.Content(UUID.randomUUID().toString(),
                                listOf(),localSession,SheetContentType.SCHEDULE_SESSION))
                            eutiViewModel.setIsChatAdded(true)

                        }
                    }
                    SheetContentType.LISTEN_TO_PODCASTS -> {
                        eutiViewModel.addToReplies(EutiChatType.Content(UUID.randomUUID().toString(),
                            listOf(),localSession,SheetContentType.LISTEN_TO_PODCASTS))
                    }
                    else -> {}
                }
            }
        }

    }
}

@Composable
fun MainSheetItem(item: EutiMainSheetItem, onItemClicked: (EutiMainSheetItem) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClicked(item) }
            .background(mainListBottomItemColor(isSystemInDarkTheme()), RoundedCornerShape(5))
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = largePadding, horizontal = smallPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(largePadding),
        ) {
            Icon(
                painter = painterResource(item.icon),
                contentDescription = "",
                tint = MaterialTheme.colors.onSurface
            )
            MediumTextBold(text = item.name)
        }

    }
}

/** @Generic Content*/


@Composable
fun GenericBottomContent(title: String, navController: NavController,eutiViewModel: EutiViewModel, onClickHome: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = eutiChatHorizontalSpacing, vertical = smallPadding),
        verticalArrangement = Arrangement.spacedBy(
            smallPadding
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        MediumTextBold(
            text = title,
            color = MaterialTheme.colors.primaryVariant,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(
                smallPadding
            )
        ) {

            /** @BackButton*/
            IconButton(
                onClick = {
                    eutiViewModel.setIsChatAdded(true)
                    navController.popBackStack()
                },
                Modifier
                    .background(transGray, RoundedCornerShape(5))
                    .weight(1f)
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "",
                        tint = MaterialTheme.colors.onSurface
                    )
                }
            }

            /** @HomeButton*/
            IconButton(
                onClick = { onClickHome() },
                Modifier
                    .background(transGray, RoundedCornerShape(5))
                    .weight(1f)
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = "",
                        tint = MaterialTheme.colors.onSurface
                    )
                }
            }

        }

    }
}



