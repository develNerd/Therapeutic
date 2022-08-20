package com.flepper.therapeutic.android.presentation.home.euti

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.flepper.therapeutic.android.presentation.home.HomeViewModel
import com.flepper.therapeutic.android.presentation.home.euti.authentication.LoginOrSignUpButtonScreen
import com.flepper.therapeutic.android.presentation.home.euti.authentication.RegistrationScreen
import com.flepper.therapeutic.android.presentation.home.euti.schedule.SelectDateScreen


enum class SheetContentType {
    ONGOING_EVENTS,
    UPCOMING_EVENTS,
    SCHEDULE_SESSION,
    WATCH_FEATURED_VIDEOS,
    DEFAULT

}

data class EutiMainSheetItem(val name: String, val icon: Int, val type: SheetContentType)

sealed class EutiScreens(
    var screenName: String,
    var bottomSheetContent: @Composable (NavController) -> Unit
) {

    /** @MainBottomContent*/
    class MainBottomContent(
        items: List<EutiMainSheetItem>,
        eutiViewModel: EutiViewModel,
        homeViewModel: HomeViewModel,
        onWatchedFeaturedVideosClick: () -> Unit,
    ) :
        EutiScreens(
            "Main Sheet",
            bottomSheetContent = { nav ->
                MainSheet(
                    items,
                    nav,
                    eutiViewModel,
                    homeViewModel,
                    onWatchedFeaturedVideosClick
                )
            })


    /** @GenericBottomContent*/

    class GenericBottomView(
        title: String = "",
        eutiViewModel: EutiViewModel,
        onHomeClicked: () -> Unit = {}
    ) : EutiScreens("GenericSheet", bottomSheetContent = { nav ->
        GenericBottomContent(
            title = title,
            navController = nav, eutiViewModel, onHomeClicked
        )
    })


    class LoginScreenView(eutiViewModel: EutiViewModel) :
        EutiScreens("LoginScreen", bottomSheetContent = { nav ->
            RegistrationScreen(
                eutiViewModel = eutiViewModel, nav
            )
        })

    class ToSignUpOrSignInScreen(eutiViewModel: EutiViewModel) :
        EutiScreens("ToSignUpOrSignInScreen", bottomSheetContent = { nav ->
            LoginOrSignUpButtonScreen(
                navController = nav,
                eutiViewModel = eutiViewModel
            )
        })

    class ScheduleSessionDateScreen(eutiViewModel: EutiViewModel) :
        EutiScreens("ScheduleSessionScreen", bottomSheetContent = { nav -> SelectDateScreen(nav,eutiViewModel)  })


}