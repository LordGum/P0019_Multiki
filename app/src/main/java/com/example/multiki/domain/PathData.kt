package com.example.multiki.domain

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import com.example.multiki.ui.theme.Black

data class PathData(
    val path: Path = Path(),
    val color: Color = Black,
    val lineWidth: Float = 1f
)