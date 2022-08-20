
package com.flepper.therapeutic.android.presentation.intro

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.activityViewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.findNavController
import com.flepper.therapeutic.android.R
import com.flepper.therapeutic.android.databinding.FragmentOnBoardingBinding
import com.flepper.therapeutic.android.presentation.MainActivityViewModel
import com.flepper.therapeutic.android.presentation.core.BaseFragment
import com.flepper.therapeutic.android.presentation.theme.LocalFontThemes
import com.flepper.therapeutic.android.presentation.theme.TherapeuticTheme
import com.flepper.therapeutic.android.presentation.theme.default


class OnBoardingFragment : BaseFragment<FragmentOnBoardingBinding>() {

   private  enum class OnBoardingDestinations(val destName:String){
        FirstScreen("first_screen"),
        SecondScreen("second_screen")
    }

    private val mainActivityViewModel:MainActivityViewModel by activityViewModels()

    override fun initUI() {
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                CompositionLocalProvider(LocalFontThemes provides default) {
                    val navController = rememberNavController()

                    TherapeuticTheme {
                        NavHost(navController = navController, startDestination = OnBoardingDestinations.FirstScreen.destName) {
                            composable(OnBoardingDestinations.FirstScreen.destName) {
                                FirstOnBoardingScreen(mainActivityViewModel.appPreferences.anonUser?.userName ?: ""){ userName ->
                                    mainActivityViewModel.saveUser(userName)
                                    navController.navigate(OnBoardingDestinations.SecondScreen.destName)
                                }
                            }
                            composable(OnBoardingDestinations.SecondScreen.destName) {
                                SecondOnBoardingScreen(){
                                    findNavController().navigate(R.id.action_onBoardingFragment_to_homeFragment)
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
    ): FragmentOnBoardingBinding {
        return FragmentOnBoardingBinding.inflate(inflater, container, false)
    }
}