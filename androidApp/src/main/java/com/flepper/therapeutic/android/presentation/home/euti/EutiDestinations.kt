package com.flepper.therapeutic.android.presentation.home.euti

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.flepper.therapeutic.android.presentation.home.HomeViewModel
import com.flepper.therapeutic.android.presentation.home.euti.authentication.LoginOrSignUpButtonScreen
import com.flepper.therapeutic.android.presentation.home.euti.authentication.RegistrationScreen
import com.flepper.therapeutic.android.presentation.home.euti.schedule.SelectDateScreen
import com.flepper.therapeutic.android.presentation.home.euti.schedule.SelectScheduleTime


enum class SheetContentType {
    ONGOING_EVENTS,
    UPCOMING_EVENTS,
    SCHEDULE_SESSION,
    LISTEN_TO_PODCASTS,
    DEFAULT

}

data class EutiMainSheetItem(val name: String, val icon: Int, val type: SheetContentType)
const val MAIN_SHEET = "Main Sheet"
sealed class EutiScreens(
    var screenName: String,
    var bottomSheetContent: @Composable (NavController) -> Unit
) {

    enum class EutiViewNames(name:String){
        MainBottomContent("Main Sheet"),
        GenericBottomView("GenericSheet"),
        LoginScreenView("LoginScreen"),
        ToSignUpOrSignInScreen("ToSignUpOrSignInScreen"),
        ScheduleSessionDateScreen("ScheduleSessionScreen"),
        ScheduleSessionTimeScreen("ScheduleTimeScreen")
    }

    /** @MainBottomContent*/
    class MainBottomContent(
        items: List<EutiMainSheetItem>,
        eutiViewModel: EutiViewModel,
        homeViewModel: HomeViewModel,
        onWatchedFeaturedVideosClick: () -> Unit,
    ) :
        EutiScreens(
            EutiViewNames.MainBottomContent.name,
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
    ) : EutiScreens(EutiViewNames.GenericBottomView.name, bottomSheetContent = { nav ->
        GenericBottomContent(
            title = title,
            navController = nav, eutiViewModel, onHomeClicked
        )
    })


    class LoginScreenView(eutiViewModel: EutiViewModel) :
        EutiScreens(EutiViewNames.LoginScreenView.name, bottomSheetContent = { nav ->
            RegistrationScreen(
                eutiViewModel = eutiViewModel, nav
            )
        })

    class ToSignUpOrSignInScreen(eutiViewModel: EutiViewModel) :
        EutiScreens(EutiViewNames.ToSignUpOrSignInScreen.name, bottomSheetContent = { nav ->
            LoginOrSignUpButtonScreen(
                navController = nav,
                eutiViewModel = eutiViewModel
            )
        })

    class ScheduleSessionDateScreen(eutiViewModel: EutiViewModel) :
        EutiScreens(EutiViewNames.ScheduleSessionDateScreen.name, bottomSheetContent = { nav -> SelectDateScreen(nav,eutiViewModel)  })


    class ScheduleSessionTimeScreen(eutiViewModel: EutiViewModel) : EutiScreens(EutiViewNames.ScheduleSessionTimeScreen.name, bottomSheetContent = {nav -> SelectScheduleTime(nav,eutiViewModel)})

}