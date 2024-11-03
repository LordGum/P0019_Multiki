package com.example.multiki.presentation

import android.graphics.Bitmap
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.multiki.R
import com.example.multiki.domain.Animation
import com.example.multiki.presentation.components.LoadingIndicator
import com.example.multiki.presentation.utils.pointerInteropFilterNative
import com.example.multiki.ui.theme.Black
import com.example.multiki.ui.theme.Blue
import com.example.multiki.ui.theme.White
import kotlinx.coroutines.delay

@Composable
fun VideoScreen(
    listAnim: List<Triple<Animation, Bitmap?, Long>>,
    onBackClick: () -> Unit
) {
    val runState = remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .background(Black)
            .fillMaxSize()
            .padding(top = 40.dp)
            .pointerInteropFilterNative {
                runState.value = false
                false
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back_arrow),
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .size(32.dp)
                .clickable { onBackClick() },
            tint = White
        )

        Spacer(modifier = Modifier.height(18.dp))

        when (listAnim.size) {
            0 -> { LoadingIndicator(modifier = Modifier.fillMaxHeight(0.85f)) }
            else -> {
                VideoBox(
                    listAnim = listAnim,
                    runState = runState.value,
                    onRunClick = { runState.value = true }
                )
            }
        }
    }
}

@Composable
fun VideoBox(
    listAnim: List<Triple<Animation, Bitmap?, Long>>,
    runState: Boolean,
    onRunClick: () -> Unit
) {
    val indImage = remember { mutableIntStateOf(0) }
    val initialBMP = ImageBitmap.imageResource(id = R.drawable.canvas_back)

    LaunchedEffect(runState) {
        while (runState) {
            delay(100)
            indImage.intValue = (indImage.intValue + 1) % listAnim.size
        }
    }

    Box(
        modifier = Modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(20.dp)),
            painter = painterResource(id = R.drawable.canvas_back),
            contentDescription = null,
            contentScale = ContentScale.None,
            alignment = Alignment.TopStart
        )

        Crossfade(
            modifier = Modifier.fillMaxWidth(),
            targetState = indImage.intValue,
            label = ""
        ) { targetState ->
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(20.dp)),
                bitmap = listAnim[targetState].second?.asImageBitmap() ?: initialBMP,
                contentDescription = null,
                contentScale = ContentScale.None,
                alignment = Alignment.TopStart
            )
        }

        if (!runState) {
            Icon(
                imageVector = Icons.Outlined.PlayArrow,
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Black.copy(alpha = 0.3f))
                    .clickable { onRunClick() },
                tint = Blue
            )
        }
    }
}