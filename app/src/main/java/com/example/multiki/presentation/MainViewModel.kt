package com.example.multiki.presentation

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.multiki.domain.PathData
import com.example.multiki.domain.Tool
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    private val _screenState = MutableStateFlow<MainScreenState>(MainScreenState.Value())
    val screenState: StateFlow<MainScreenState> = _screenState

    private val _pathData = MutableStateFlow(PathData())
    val pathData: StateFlow<PathData> = _pathData

    private var _pathList = MutableStateFlow(listOf(PathData()))
    val pathList: StateFlow<List<PathData>> = _pathList

    private val _paletteState = MutableStateFlow(false)
    val paletteState: StateFlow<Boolean> = _paletteState

    private val _widthLineState = MutableStateFlow(false)
    val widthLineState: StateFlow<Boolean> = _widthLineState

    fun changeColor(color: Color) {
        _screenState.update {
            MainScreenState.Value(
                activeColor = color,
                activeTool = Tool.COLOR_SIMPLE
            )
        }
        _pathData.update { _pathData.value.copy(color = color) }
    }

    fun changeTool(tool: Tool) {
        if (_screenState.value is MainScreenState.Value) {
            val oldState = _screenState.value as MainScreenState.Value
            _screenState.update { oldState.copy(activeTool = tool) }
        } else {
            _screenState.update { MainScreenState.Value(activeTool = tool) }
        }

        if(tool == Tool.COLOR_SIMPLE) _paletteState.update { !_paletteState.value }
        else if(tool == Tool.COLOR_HARD) _paletteState.update { true }
        else _paletteState.update { false }

        if (tool == Tool.PEN) _widthLineState.update { !_widthLineState.value }
        else _widthLineState.update { false }
    }

    fun changeLineWidth(lineWidth: Float) {
        _pathData.update { pathData.value.copy(lineWidth = lineWidth) }
    }

    fun addPath(pathData: PathData) {
        val newList = _pathList.value.toMutableList()
        newList.add(pathData)
        _pathList.update { newList }

        Log.d(
            "lama",
            "add ${_pathList.value.size}"
        )
    }

    fun removeLastPath() {
        val newList = _pathList.value.toMutableList()
        if (newList.size > 0) {
            newList.removeIf{ pathD ->
                newList[newList.size - 1] == pathD
            }
        }
        _pathList.update { newList }
    }

    fun returnLastPath() {

    }
}