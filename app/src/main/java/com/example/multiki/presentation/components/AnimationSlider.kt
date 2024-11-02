package com.example.multiki.presentation.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.multiki.R
import com.example.multiki.domain.Animation
import com.example.multiki.ui.theme.Black
import com.example.multiki.ui.theme.BoardGrey
import com.example.multiki.ui.theme.LimeGreen

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AnimationSlider(
    list: List<Triple<Animation, Bitmap?, Long>>,
    onAnimClick: (Animation?) -> Unit,
    activeAnim: Animation?
) {

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp)
            .padding(top = 115.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(
                width = 1.dp,
                color = BoardGrey,
                shape = RoundedCornerShape(4.dp)
            )
            .background(Black.copy(alpha = 0.14f))
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(items = list, key = { it.third}) {
            val initialBMP = ImageBitmap.imageResource(id = R.drawable.canvas_back)
            AnimSlide(
                anim = it.first,
                bitmap = it.second?.asImageBitmap() ?: initialBMP,
                number = it.third,
                onAnimClick = onAnimClick,
                activityAnim = activeAnim
            )
        }
    }
}

@Composable
fun AnimSlide(
    anim: Animation?,
    activityAnim: Animation?,
    bitmap: ImageBitmap,
    number: Long,
    onAnimClick: (Animation?) -> Unit
) {
    val height = (bitmap.height / 25).dp
    val width = (bitmap.width / 25).dp
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .size(height = height, width = width)
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(4.dp),
                color = if(anim == activityAnim) LimeGreen else Black,
            )
            .clickable { onAnimClick(anim) }
    ) {
        Image(
            bitmap = bitmap,
            contentDescription = null
        )
        Text(
            text = number.toString(),
            color = Black,
            modifier = Modifier
                .width(width)
                .background(Color.LightGray),
            textAlign = TextAlign.Center
        )
    }
}