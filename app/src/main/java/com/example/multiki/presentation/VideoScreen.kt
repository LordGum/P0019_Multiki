package com.example.multiki.presentation

import android.graphics.Bitmap
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.example.multiki.R
import com.example.multiki.domain.Animation
import kotlinx.coroutines.delay

@Composable
fun VideoScreen(
    modifier: Modifier,
    listAnim: List<Triple<Animation, Bitmap?, Long>>
) {
    val indImage = remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(100)
            indImage.value = (indImage.value + 1) % listAnim.size
        }
    }

    val initialBMP = ImageBitmap.imageResource(id = R.drawable.canvas_back)

    Crossfade(
        modifier = modifier,
        targetState = indImage.value,
        label = ""
    ) { targetState ->
        Image(
            modifier = modifier
                .fillMaxHeight(0.85f)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(20.dp)),
            bitmap = listAnim[targetState].second?.asImageBitmap() ?: initialBMP,
            contentDescription = null,
            contentScale = ContentScale.None,
            alignment = Alignment.TopStart
        )
    }
}