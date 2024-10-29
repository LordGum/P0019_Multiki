package com.example.multiki.presentation

import androidx.compose.ui.graphics.Color
import com.example.multiki.domain.Tool
import com.example.multiki.ui.theme.Blue

sealed class MainScreenState  {
    data class Value (
        var activeTool: Tool = Tool.PEN,
        var activeColor: Color = Blue
    ): MainScreenState()
}