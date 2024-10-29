package com.example.multiki.domain

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import com.example.multiki.ui.theme.Blue

data class PathData(
    val path: Path = Path(),
    val color: Color = Blue,
    val lineWidth: Float = 7f
)