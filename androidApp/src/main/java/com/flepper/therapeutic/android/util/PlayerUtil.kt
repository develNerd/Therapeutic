package com.flepper.therapeutic.android.util

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

object PlayerUtil {

    var player: ExoPlayer? = null
    private var playWhenReady = true
    private var currentItem = 0
    private var playbackPosition = 0L
    var videoLink:String = "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4"

    fun releasePlayer() {
        player?.let { exoPlayer ->
            playbackPosition = exoPlayer.currentPosition
            currentItem = exoPlayer.currentMediaItemIndex
            playWhenReady = exoPlayer.playWhenReady
            exoPlayer.release()
        }
        player = null
    }

    fun initPlayer(context: Context,link:String = videoLink):ExoPlayer{
       player = ExoPlayer.Builder(context)
            .build()
            .also { exoPlayer ->
                val mediaItem =
                    MediaItem.fromUri(link)
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.videoScalingMode = MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                exoPlayer.playWhenReady = true
                exoPlayer.seekTo(0, 0L)
                exoPlayer.prepare()
            }

        return player!!
    }
}