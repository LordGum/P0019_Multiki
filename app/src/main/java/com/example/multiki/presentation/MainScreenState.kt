package com.example.multiki.presentation

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import com.example.multiki.domain.Animation
import com.example.multiki.domain.PathData
import com.example.multiki.domain.Tool
import com.example.multiki.ui.theme.Black

sealed class MainScreenState  {
    data class Value (
        var activeTool: Tool = Tool.PEN,
        var activeColor: Color = Black,
        var activeAnim: Animation? = null,
        var pathData: PathData = PathData(),
        var bitmapImage: Bitmap? = null,
        var pathForwardList: List<PathData> = listOf()
    ): MainScreenState()

    data object Loading: MainScreenState()

    data object Video : MainScreenState()
}