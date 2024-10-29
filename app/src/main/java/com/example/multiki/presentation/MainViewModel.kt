package com.example.multiki.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.multiki.domain.Tool
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    private val _screenState = MutableStateFlow<MainScreenState>(MainScreenState.Value())
    val screenState: StateFlow<MainScreenState> = _screenState

    fun changeColor(color: Color) {
        _screenState.update {
            MainScreenState.Value(
                activeColor = color,
                activeTool = Tool.COLOR_SIMPLE
            )
        }
    }

    fun changeTool(tool: Tool) {
        if (_screenState.value is MainScreenState.Value) {
            val oldState = _screenState.value as MainScreenState.Value
            _screenState.update { oldState.copy(activeTool = tool) }
        } else {
            _screenState.update { MainScreenState.Value(activeTool = tool) }
        }
    }
}