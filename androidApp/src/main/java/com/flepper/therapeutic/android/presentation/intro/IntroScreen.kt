package com.flepper.therapeutic.android.presentation.intro

import android.graphics.Outline
import android.media.MediaPlayer
import android.view.View
import android.view.ViewOutlineProvider
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.withResumed
import androidx.lifecycle.withStarted
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.flepper.therapeutic.android.R
import com.flepper.therapeutic.android.databinding.PlayerLayoutBinding
import com.flepper.therapeutic.android.presentation.theme.*
import com.flepper.therapeutic.android.presentation.widgets.*
import com.flepper.therapeutic.android.util.PlayerUtil
import com.flepper.therapeutic.android.util.resolveVideoUrl

@Composable
fun IntroScreen(onContinue:(Boolean) -> Unit) {
    var showDialog by remember {
        mutableStateOf(false)
    }


    Box(modifier = Modifier.fillMaxSize()) {
        if (showDialog) {
            VideoView(onContinue)
        }
        Image(
            painter = painterResource(id = R.drawable.img_into_background),
            contentScale = ContentScale.Crop,
            contentDescription = "",
            modifier = Modifier.fillMaxSize()
        )
        Box(
            Modifier
                .fillMaxSize()
                .background(transBlack)
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = mediumPadding, vertical = spacing32dp),
            verticalArrangement = Arrangement.spacedBy(
                mediumPadding
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(
                    largePadding
                ), verticalAlignment = Alignment.CenterVertically
            ) {
                LargeBoldText(
                    text = stringResource(R.string.welcome_to),
                    fontSize = 24.sp,
                    color = colorWhite
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_welcome_hand),
                    contentDescription = ""
                )
            }
            LargeBoldText(
                text = stringResource(R.string.app_name),
                fontSize = 28.sp,
                color = MaterialTheme.colors.primary,
            )
            RegularText(
                text = stringResource(R.string.intro_desc),
                color = colorWhite,
                size = 16.sp,
                modifier = Modifier.padding(bottom = smallPadding)
            )
            Row() {
                RoundedCornerButton(
                    text = stringResource(R.string.get_started),
                    roundness = 100,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.48f)
                ) {
                    onContinue(false)
                }
                Spacer(modifier = Modifier.weight(0.04f))
                RoundedOutlineButton(
                    text = stringResource(R.string.watch_into_video),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.48f)
                ) {
                    showDialog = true
                }
            }
           
        }
    }
}

@Composable
fun VideoView(onContinue: (Boolean) -> Unit) {

    val context = LocalContext.current
    val player = PlayerUtil.initPlayer(context, resolveVideoUrl(context,"into_video.mp4"))

    Dialog(onDismissRequest = { /*TODO*/ }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = spacing32dp)
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .background(Color.Transparent, shape = RoundedCornerShape(10)),
                verticalArrangement = Arrangement.spacedBy(
                    largePadding
                )
            ) {
                AndroidViewBinding(PlayerLayoutBinding::inflate) {
                    videoView.apply {
                        this.player = player
                        setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                        videoSurfaceView?.outlineProvider = object : ViewOutlineProvider() {
                            override fun getOutline(p0: View?, outline: Outline?) {
                                outline?.setRoundRect(0, 0, p0!!.width, p0.height, 15F);
                            }
                        }
                        clipToOutline = true
                        videoSurfaceView?.clipToOutline = true
                    }
                }
                
                RoundedCornerButtonWithIcon(modifier = Modifier.fillMaxWidth(), backgroundColor = transWhite,text = "Continue", icon = Icons.Default.KeyboardArrowRight) {
                    onContinue(true)
                }
            }

        }

    }

}