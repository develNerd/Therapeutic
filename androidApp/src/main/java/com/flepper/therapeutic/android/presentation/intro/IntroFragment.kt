package com.flepper.therapeutic.android.presentation.intro

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.activityViewModels
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.navigation.fragment.findNavController
import com.flepper.therapeutic.android.R
import com.flepper.therapeutic.android.databinding.FragmentIntroBinding
import com.flepper.therapeutic.android.presentation.MainActivityViewModel
import com.flepper.therapeutic.android.presentation.core.BaseFragment
import com.flepper.therapeutic.android.presentation.theme.LocalFontThemes
import com.flepper.therapeutic.android.presentation.theme.TherapeuticTheme
import com.flepper.therapeutic.android.presentation.theme.default
import com.flepper.therapeutic.android.util.PlayerUtil

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [IntroFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IntroFragment : BaseFragment<FragmentIntroBinding>() {

    val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private var isIntoVideoPlaying:Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    companion object {

        @JvmStatic
        fun newInstance() = IntroFragment()
    }




    override fun initUI() {
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                CompositionLocalProvider(LocalFontThemes provides default) {
                    TherapeuticTheme {
                        IntroScreen(onContinue = {isContinue ->
                            if (isContinue){
                                PlayerUtil.releasePlayer()
                            }
                            findNavController().navigate(R.id.action_intro_fragment_to_onBoardingFragment)
                        })
                    }
                }
            }
        }
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentIntroBinding {
        return FragmentIntroBinding.inflate(inflater, container, false)
    }
}