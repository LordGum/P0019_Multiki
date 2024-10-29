package com.example.multiki.presentation.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Label
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.multiki.R
import com.example.multiki.ui.theme.Black
import com.example.multiki.ui.theme.BoardGrey

@Composable
fun PenWidthLine(
    sliderPosition: Float,
    onLineWidthChange: (Float) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
            .padding(vertical = 16.dp)
            .padding(top = 670.dp)
            .background(Black.copy(alpha = 0.14f))
            .clip(RoundedCornerShape(4.dp))
            .border(
                width = 1.dp,
                color = BoardGrey,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        SliderWithCustomThumbSample(
            currentPosition = sliderPosition,
            onChange = { lineWidth ->
                onLineWidthChange(lineWidth)
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderWithCustomThumbSample(
    currentPosition: Float,
    onChange: (Float) -> Unit
) {
    var sliderPosition by remember { mutableFloatStateOf(1 - currentPosition/100) }
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text("Line width: ${(100 - sliderPosition * 100).toInt()}")
        Slider(
            value = sliderPosition,
            onValueChange = {
                val tempPos = if (it > 0) it else 1f
                Log.d(
                    "lama",
                    "tempPos = $tempPos"
                )
                sliderPosition = tempPos
                onChange(100 - tempPos * 100)
            },
            valueRange = 0f..1f,
            interactionSource = interactionSource,
            onValueChangeFinished = {},
            thumb = {
                Label(
                    label = {},
                    interactionSource = interactionSource
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_thumb),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                }
            },
            track = {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(id = R.drawable.ic_line),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        )
    }
}