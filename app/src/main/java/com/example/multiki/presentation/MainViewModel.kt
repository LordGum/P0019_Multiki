package com.example.multiki.presentation

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.multiki.domain.PathData
import com.example.multiki.domain.Tool
import com.example.multiki.presentation.components.getBitMap
import com.example.multiki.presentation.components.saveBitmapToFile
import com.example.multiki.ui.theme.Black
import com.example.multiki.ui.theme.Blue
import com.example.multiki.ui.theme.White
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
   private val application: Application
) : AndroidViewModel(application) {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        Log.d("MainViewModel", "Exception caught by exception handler")
    }
    private val coroutineContext = Dispatchers.IO + exceptionHandler

    private val _screenState = MutableStateFlow<MainScreenState>(MainScreenState.Value())
    val screenState: StateFlow<MainScreenState> = _screenState

    private val _pathData = MutableStateFlow(PathData())
    val pathData: StateFlow<PathData> = _pathData

    private val _bitmapImage = MutableStateFlow<Bitmap?>(null)
    val bitmapImage: StateFlow<Bitmap?> = _bitmapImage

    val pathList = mutableStateListOf<PathData>()

    private val _saveFlag = MutableStateFlow(false)
    val saveFlag: StateFlow<Boolean> = _saveFlag

    private var _pathForwardList = MutableStateFlow<List<PathData>>(listOf())
    val pathForwardList: StateFlow<List<PathData>> = _pathForwardList

    private val _paletteState = MutableStateFlow(false)
    val paletteState: StateFlow<Boolean> = _paletteState

    private val _widthLineState = MutableStateFlow(false)
    val widthLineState: StateFlow<Boolean> = _widthLineState

    private val _sliderState = MutableStateFlow(false)
    val sliderState: StateFlow<Boolean> = _sliderState


    fun changeColor(color: Color, tool: Tool = Tool.COLOR_SIMPLE) {
        _screenState.update {
            MainScreenState.Value(
                activeColor = color,
                activeTool = tool
            )
        }
        _pathData.update { _pathData.value.copy(color = color) }
    }

    fun changeTool(tool: Tool) {
        if (_screenState.value is MainScreenState.Value) {
            when (tool) {
                Tool.PEN -> {
                    _screenState.update { MainScreenState.Value(Tool.PEN) }
                    changeColor(Black, Tool.PEN)
                    changeLineWidth(WIDTH_PEN)
                }

                Tool.BRUSH -> {
                    val oldValue = _screenState.value as MainScreenState.Value
                    if (oldValue.activeTool != tool)
                        changeLineWidth(WIDTH_BRUSH)
                    _screenState.update { MainScreenState.Value(Tool.BRUSH) }
                    changeColor(Blue, Tool.BRUSH)
                }

                else -> {
                    val oldState = _screenState.value as MainScreenState.Value
                    _screenState.update { oldState.copy(activeTool = tool) }
                }
            }
        }

        when (tool) {
            Tool.COLOR_SIMPLE -> _paletteState.update { !_paletteState.value }
            Tool.COLOR_HARD -> _paletteState.update { true }
            else -> _paletteState.update { false }
        }

        if (tool == Tool.BRUSH || tool == Tool.ERASER) _widthLineState.update { !_widthLineState.value }
        else _widthLineState.update { false }

        if (tool == Tool.ERASER) {
            val oldPathData = _pathData.value
            _pathData.update {
                oldPathData.copy(
                    isEraser = true,
                    color = White,
                    lineWidth = WIDTH_ERASER
                )
            }
        } else {
            val oldPathData = _pathData.value
            _pathData.update { oldPathData.copy(isEraser = false) }
        }
    }

    fun changeLineWidth(lineWidth: Float) {
        _pathData.update { pathData.value.copy(lineWidth = lineWidth) }
    }

    fun addPath(pathData: PathData) {
        pathList.add(pathData)
        _pathForwardList.update { listOf() }
    }

    fun addAnimation(imageBitmap: ImageBitmap) {
        saveBitmapToFile(
            bitmap = imageBitmap.asAndroidBitmap(),
            application = application,
            fileName = "new_haha_file"
        )
        changeSaveFlag(false)
        pathList.clear()
        _bitmapImage.value = null
        Log.d("lama", "add animation")
    }

    fun loadAnimation(
        fileName: String,
        application: Application
    )
    = viewModelScope.launch (coroutineContext) {
        _bitmapImage.update { getBitMap(fileName, application) }
    }

    fun getBitMapVM(fileName: String) = viewModelScope.async(exceptionHandler) {
        getBitMap(fileName, application)
    }

    fun deleteAnimation() {
        pathList.clear()
    }

    fun removeLastPath() {
        val forwardList = _pathForwardList.value.toMutableList()
        forwardList.add(pathList.last())
        _pathForwardList.update { forwardList }

        pathList.removeIf { pathD ->
            pathList[pathList.size - 1] == pathD
        }
    }

    fun returnLastPath() {
        val forwardList = _pathForwardList.value.toMutableList()
        if (forwardList.isNotEmpty()) {
            pathList.add(forwardList.last())
            forwardList.removeLast()
            _pathForwardList.update { forwardList }
        }
    }

    fun onTapFilter() {
        _paletteState.update { false }
        _widthLineState.update { false }
        _sliderState.update { false }
    }

    fun changeSaveFlag(flag: Boolean) {
        _saveFlag.update { flag }
    }

    fun changeSliderState() {
        _sliderState.value = !_sliderState.value
    }

    companion object {
        const val WIDTH_PEN = 1f
        const val WIDTH_BRUSH = 50f
        const val WIDTH_ERASER = 40f
    }
}