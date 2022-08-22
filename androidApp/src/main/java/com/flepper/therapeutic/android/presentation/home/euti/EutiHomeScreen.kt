package com.flepper.therapeutic.android.presentation.home.euti

import android.content.Context
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.flepper.therapeutic.android.R
import com.flepper.therapeutic.android.presentation.home.BottomSheetGestureView
import com.flepper.therapeutic.android.presentation.home.EventChipItem
import com.flepper.therapeutic.android.presentation.home.HomeViewModel
import com.flepper.therapeutic.android.presentation.theme.*
import com.flepper.therapeutic.android.presentation.widgets.MediumTextBold


/** @EutiHome */

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EutiHome(homeNavController:NavController,eutiViewModel: EutiViewModel,homeViewModel: HomeViewModel) {


    val navController = rememberNavController()
    val mainSheetItems by homeViewModel.mainSheetItems.collectAsState()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val mainScreen = EutiScreens.MainBottomContent(
        items = mainSheetItems,
        eutiViewModel = eutiViewModel,
        homeViewModel
    ) {
        /** @FeaturedItemClicked Here*/
    }

    val genericTitle by eutiViewModel.genericTitle.collectAsState()

    val genericView = EutiScreens.GenericBottomView(
        title = genericTitle,
        eutiViewModel
    ) {
        homeNavController.popBackStack()
    }

    val signInScreen = EutiScreens.LoginScreenView(eutiViewModel)

    val toSignInOrSignUp= EutiScreens.ToSignUpOrSignInScreen(eutiViewModel)

    val selectDateScreen = EutiScreens.ScheduleSessionDateScreen(eutiViewModel)

    val selectTimeScreen = EutiScreens.ScheduleSessionTimeScreen(eutiViewModel)

    /** @LOLYouCanMakeThisDynamic -> Populate as needed - Not Scalable as at now*/
    val bottomDestinations = listOf(
        mainScreen,
        genericView,
        signInScreen,
        toSignInOrSignUp,
        selectDateScreen,
        selectTimeScreen
    )



    val eutiChats by eutiViewModel.eutiReplies.collectAsState()
    var chatViewPaddingBottom by remember {
        mutableStateOf(100.dp)
    }

    Scaffold(
        topBar = {
                 EutToolbar(smallTitle = stringResource(id = R.string.euti),icon = R.drawable.img_euti) {
                     homeNavController.popBackStack()
                 }
        },
        bottomBar = {
            /** @BottomSheetContent Goes Here*/


            val context = LocalContext.current
            Surface(elevation = size10dp, shape = RoundedCornerShape(5),color =MaterialTheme.colors.surface , modifier = Modifier) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = spacing2dp)
                    .height(IntrinsicSize.Min)
                    .animateContentSize()
                    .onGloballyPositioned {
                        chatViewPaddingBottom = toDp(it.size.height, context).dp + mediumPadding

                    }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(
                    spacing5dp)) {
                    BottomSheetGestureView()
                    NavHost(
                        navController = navController,
                        startDestination = mainScreen.screenName
                    ) {
                        bottomDestinations.forEachIndexed { index, eutiScreen ->
                            composable(eutiScreen.screenName){
                                eutiScreen.bottomSheetContent(navController)
                            }
                        }
                    }
                }
            }
        }
    ) {
        it.calculateBottomPadding()
        Column() {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = transGray)
                    .height(1.dp)
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = transGray)
                    .height(1.dp)
            )
            ChatView(
                modifier = Modifier
                    .padding(top = mediumPadding, bottom = chatViewPaddingBottom)
                    .weight(1f),
                eutiChats = eutiChats,
                paddingBottom =chatViewPaddingBottom,
                eutiViewModel = eutiViewModel,
                homeViewModel
            )

        }


    }
}




/** @EutiHomeToolbar*/

@Composable
fun EutToolbar(smallTitle: String = "", @DrawableRes icon: Int, onBackClicked: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = spacing3dp)) {
        IconButton(
            onClick = onBackClicked, modifier = Modifier
                .padding(start = spacing3dp)
                .align(
                    Alignment.CenterStart
                )
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.app_name),
                tint = MaterialTheme.colors.onBackground
            )
        }

        val spacing1dp = 1.dp
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(spacing1dp)
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier =
                Modifier
                    .size(size24dp)
                    .clip(RoundedCornerShape(100))
                    .align(Alignment.CenterHorizontally)
            )
            if (smallTitle.isNotEmpty()) {
                MediumTextBold(
                    text = smallTitle,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }


    }
}

fun toDp(px:Int,context:Context):Float{
    val density: Float = context.resources.displayMetrics.density

    return   px / (density)
}