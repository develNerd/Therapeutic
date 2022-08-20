package com.flepper.therapeutic.android.presentation.home.featured

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.flepper.therapeutic.android.R
import com.flepper.therapeutic.android.databinding.FragmentFeaturedDetailedBinding
import com.flepper.therapeutic.android.databinding.FragmentHomeBinding
import com.flepper.therapeutic.android.presentation.core.BaseFragment
import com.flepper.therapeutic.android.presentation.home.FeaturedContentColumn
import com.flepper.therapeutic.android.presentation.home.HomeViewModel
import com.flepper.therapeutic.android.presentation.theme.TherapeuticTheme
import com.flepper.therapeutic.android.presentation.theme.size24dp
import com.flepper.therapeutic.android.presentation.theme.smallPadding
import com.flepper.therapeutic.android.presentation.theme.white
import com.flepper.therapeutic.android.util.resolveVideoUrl
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FeaturedDetailedFragment : BaseFragment<FragmentFeaturedDetailedBinding>() {

    private var player: ExoPlayer? = null
    private var playbackPosition = 0L
    private var currentVideoItem = 0
    private var playWhenReady = true

    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.home)
    private fun initializePlayer(onInitPlayer: (Player) -> Unit = {}) {
        lifecycleScope.launch {
            homeViewModel.currentFeaturedContent.collectLatest { currentItem ->
                if (currentItem.isNotEmpty() && player == null) {
                    player = ExoPlayer.Builder(requireContext())
                        .build()
                        .also { exoPlayer ->
                            binding.videoView.player = exoPlayer
                            val mediaItem =
                                MediaItem.fromUri(
                                    resolveVideoUrl(
                                        requireContext(),
                                        currentItem.first().video
                                    )
                                )
                            Log.e(
                                "Video",
                                resolveVideoUrl(requireContext(), currentItem.first().video)
                            )
                            exoPlayer.videoScalingMode =
                                MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                            exoPlayer.setMediaItem(mediaItem)
                            exoPlayer.playWhenReady = playWhenReady
                            exoPlayer.seekTo(currentVideoItem, playbackPosition)
                            exoPlayer.prepare()
                            onInitPlayer(exoPlayer)
                        }
                }
            }
        }

    }


    override fun initUI() {
        initializePlayer()
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                TherapeuticTheme() {
                    FeaturedContentColumn(homeViewModel = homeViewModel)
                }
            }
        }
        binding.videoButtons.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                TherapeuticTheme() {
                    IconButton(
                        onClick =
                        {
                            findNavController().popBackStack()
                        }, modifier = Modifier.padding(smallPadding)
                    ) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "",
                            tint = white,
                            modifier = Modifier.size(
                                size24dp
                            )
                        )
                    }
                }
            }
        }
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFeaturedDetailedBinding {
        return FragmentFeaturedDetailedBinding.inflate(inflater, container, false)
    }

    override fun onResume() {
        super.onResume()
        //
        if (player == null) {
            initializePlayer()
        }
    }

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            playbackPosition = exoPlayer.currentPosition
            currentVideoItem = exoPlayer.currentMediaItemIndex
            playWhenReady = exoPlayer.playWhenReady
            exoPlayer.release()
        }
        player = null
    }

    private fun resetPlayer() {
        player?.release()
        player = null
        playbackPosition = 0L
        currentVideoItem = 0
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

}