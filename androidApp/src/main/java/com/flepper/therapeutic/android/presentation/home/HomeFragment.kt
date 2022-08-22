package com.flepper.therapeutic.android.presentation.home

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.*
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.flepper.therapeutic.android.BuildConfig
import com.flepper.therapeutic.android.R
import com.flepper.therapeutic.android.databinding.FragmentHomeBinding
import com.flepper.therapeutic.android.presentation.MainActivityViewModel
import com.flepper.therapeutic.android.presentation.core.BaseFragment
import com.flepper.therapeutic.android.presentation.home.euti.EutiHome
import com.flepper.therapeutic.android.presentation.home.euti.EutiViewModel
import com.flepper.therapeutic.android.presentation.intro.FirstOnBoardingScreen
import com.flepper.therapeutic.android.presentation.intro.OnBoardingFragment
import com.flepper.therapeutic.android.presentation.intro.SecondOnBoardingScreen
import com.flepper.therapeutic.android.presentation.theme.*
import com.flepper.therapeutic.android.presentation.widgets.MediumTextBold
import com.flepper.therapeutic.android.presentation.widgets.RegularText
import kotlinx.coroutines.launch



enum class MainHomeDestinations(val destName:String){
    VideoDetailScreen("video_detailed_screen"),
    HomeScreen("home_screen"),
    EutiScreen("euti_screen")
}


enum class BottomSheetContentType{
    EVENT,
    APPOINTMENT
}

class HomeFragment : BaseFragment<FragmentHomeBinding>() {



    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private val homeViewModel:HomeViewModel by navGraphViewModels(R.id.home)
    private val eutiViewModel:EutiViewModel by viewModels()





    @OptIn(ExperimentalMaterialApi::class)
    override fun initUI() {
        /*Get featured Content*/



        val mainAppNavController = findNavController()
        homeViewModel.getFeaturedContent()
        homeViewModel.getEvents()
        homeViewModel.getSessionLocal()
        homeViewModel.appPreferences.isBeenToDashboard = true
        mainActivityViewModel.getTeamMembers()
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                CompositionLocalProvider(LocalFontThemes provides default) {

                    TherapeuticTheme {
                        val navController = rememberNavController()
                        val items = listOf(
                            BottomScreen.Home,
                            BottomScreen.Euti
                        )
                        val state = rememberModalBottomSheetState(
                            initialValue = ModalBottomSheetValue.Hidden)
                        val scope = rememberCoroutineScope()
                        var isChanged by remember {
                            mutableStateOf(false)
                        }



                        ModalBottomSheetLayout(
                            sheetState = state,
                            sheetContent = {
                                val selectedEvent by homeViewModel.selectedEvent.collectAsState()
                                val changeMade by homeViewModel.changeMade.collectAsState()
                                val sessionChangeMade by homeViewModel.sessionChangeMade.collectAsState()
                                val session by homeViewModel.localSession.collectAsState()
                                val currentBottomSheetContentType by homeViewModel.currentBottomSheetContentType.collectAsState()
                                val collapseBottomSheet by homeViewModel.collapseBottomSheet.collectAsState()

                                if (collapseBottomSheet){
                                    LaunchedEffect(key1 =  changeMade, block = {
                                        state.hide()
                                    })
                                }

                                if (currentBottomSheetContentType == BottomSheetContentType.EVENT){
                                    if (selectedEvent.isNotEmpty()){
                                        DetailedEventBottomSheet(worldWideEvent = selectedEvent.first(),homeViewModel,state)
                                        LaunchedEffect(key1 =  changeMade, block = {
                                            state.show()
                                        })


                                    }else{
                                        Spacer(modifier = Modifier.size(size56dp))

                                    }
                                }else{
                                    if (session.isNotEmpty()){
                                        DetailedSessionBottomSheet(session.first(),homeViewModel,state)
                                        LaunchedEffect(key1 =  sessionChangeMade, block = {
                                            state.show()
                                        })
                                        //homeViewModel.getTeamembersLocal(session.first().appointmentSegments?.first()?.teamMemberId ?: "")

                                    }else if (session.isEmpty()){

                                        Spacer(modifier = Modifier.size(size56dp))
                                    }
                                }
                                val coroutinesScope = rememberCoroutineScope()

                                BackHandler(state.isVisible) {
                                    coroutinesScope.launch {
                                        state.hide()
                                    }
                                }

                            },
                            sheetBackgroundColor = MaterialTheme.colors.background,
                            scrimColor = transBlack.copy(alpha = 0.5F)
                        )
                        {
                            NavHost(navController = navController, startDestination  = MainHomeDestinations.HomeScreen.destName)
                            {
                                composable(MainHomeDestinations.HomeScreen.destName){
                                    Scaffold(
                                        bottomBar = {
                                            HomeBottomNav(items = items){screen ->
                                                /** Hide bottom Sheet by setting selected to null*/
                                                homeViewModel.setSelectedEvent(null)
                                                val route = when(screen){
                                                    is BottomScreen.Home -> MainHomeDestinations.HomeScreen.destName
                                                    is BottomScreen.Euti -> MainHomeDestinations.EutiScreen.destName
                                                }
                                                navController.navigate(route) {
                                                    // Pop up to the start destination of the graph to
                                                    // avoid building up a large stack of destinations
                                                    // on the back stack as users select items
                                                    popUpTo(navController.graph.findStartDestination().id) {
                                                        saveState = true
                                                    }
                                                    // Avoid multiple copies of the same destination when
                                                    // reselecting the same item
                                                    launchSingleTop = true
                                                    // Restore state when reselecting a previously selected item
                                                    restoreState = true
                                                }
                                            }
                                        }
                                    ) {
                                        HomeScreen(homeViewModel = homeViewModel){
                                            mainAppNavController.navigate(R.id.featuredDetailedFragment)
                                        }
                                    }

                                }
                                composable(MainHomeDestinations.EutiScreen.destName){
                                    EutiHome(homeNavController = navController, eutiViewModel = eutiViewModel,homeViewModel = homeViewModel)
                                }


                            }
                        }




                    }
                }
            }
        }
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater,container,false)
    }


    private fun NavGraphBuilder.eutiBottomNavGraph(navController: NavController,homeViewModel: HomeViewModel){
        navigation(startDestination =BottomScreen.Euti.route, route =  MainHomeDestinations.EutiScreen.destName) {

        }
    }

}

sealed class BottomScreen(val route: String, @DrawableRes val icon: Int) {
    object Home : BottomScreen("Home", R.drawable.ic_home)
    object Euti : BottomScreen("Euti", R.drawable.img_euti)
}


@Composable
fun HomeTopBar(){

}

