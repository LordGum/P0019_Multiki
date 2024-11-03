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
import com.example.multiki.data.RepositoryImpl
import com.example.multiki.domain.Animation
import com.example.multiki.domain.PathData
import com.example.multiki.domain.Tool
import com.example.multiki.presentation.utils.deleteFile
import com.example.multiki.presentation.utils.getBitMap
import com.example.multiki.presentation.utils.saveBitmapToFile
import com.example.multiki.ui.theme.Black
import com.example.multiki.ui.theme.Blue
import com.example.multiki.ui.theme.White
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class MainViewModel(
   private val application: Application
) : AndroidViewModel(application) {

    private val repository = RepositoryImpl(application)

    val animList = repository.getAnimList()

    private val _pathData = MutableStateFlow(PathData())
    val pathData: StateFlow<PathData> = _pathData

    private val _bitmapImage = MutableStateFlow<Bitmap?>(null)
    val bitmapImage: StateFlow<Bitmap?> = _bitmapImage

    private val _activeAnim = MutableStateFlow<Animation?>(null)

    private var _pathForwardList = MutableStateFlow<List<PathData>>(listOf())
    val pathForwardList: StateFlow<List<PathData>> = _pathForwardList

    val pathList = mutableStateListOf<PathData>()

    private val _saveFlag = MutableStateFlow(false)
    val saveFlag: StateFlow<Boolean> = _saveFlag

    private val _paletteState = MutableStateFlow(false)
    val paletteState: StateFlow<Boolean> = _paletteState

    private val _widthLineState = MutableStateFlow(false)
    val widthLineState: StateFlow<Boolean> = _widthLineState

    private val _sliderState = MutableStateFlow(false)
    val sliderState: StateFlow<Boolean> = _sliderState

    private val _videoRunState = MutableStateFlow(false)
    val videoRunState: StateFlow<Boolean> = _videoRunState

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        Log.d("MainViewModel", "Exception caught by exception handler")
    }
    private val coroutineContext = Dispatchers.IO + exceptionHandler

    private val _screenState = MutableStateFlow<MainScreenState>(MainScreenState.Loading)
    val screenState: StateFlow<MainScreenState> = combine(
        _screenState,
        _activeAnim,
        _bitmapImage,
        _pathForwardList,
        _pathData
    ) { state, activeAnim, bitmap, pathForwardList, pathData ->
        if(state is MainScreenState.Value) {
            state.copy(
                activeColor = state.activeColor,
                activeTool = state.activeTool,
                activeAnim = activeAnim,
                bitmapImage = bitmap,
                pathForwardList = pathForwardList,
                pathData = pathData
            )
        } else {
            state
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainScreenState.Value()
    )

    init {
        if(_screenState.value is MainScreenState.Value) {
            val state = _screenState.value as MainScreenState.Value
            state.activeAnim?.fileName?.let { fileName ->
                viewModelScope.launch(coroutineContext) {
                    val bitmap = getBitMapVM(fileName).await()
                    _bitmapImage.update { bitmap }
                }
            }
        } else {
            _screenState.update { MainScreenState.Value() }
        }
    }

    fun changeScreenState(state: MainScreenState) {
        _screenState.update { state }
    }

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

    fun addAnimation(imageBitmap: ImageBitmap, activeAnim: Animation?) {
        val createAt = activeAnim?.createAt ?: Date().time
        val fileName = activeAnim?.fileName ?: repository.getFileName(createAt)

        viewModelScope.launch(coroutineContext) {
            saveBitmapToFile(
                bitmap = imageBitmap.asAndroidBitmap(),
                application = application,
                fileName = fileName
            )
            val animation = Animation(
                createAt = createAt,
                fileName = fileName
            )
            repository.addAnim(animation)
        }
        changeSaveFlag(false)
        pathList.clear()
        _bitmapImage.value = null
        _activeAnim.update { null }
    }

    suspend fun loadAnimList(animList: List<Animation>): List<Triple<Animation, Bitmap?, Long>>  {
        val list = mutableListOf<Triple<Animation, Bitmap?, Long>>()
        for (i in animList.indices) {
            val anim = animList[i]
            val bitmap = getBitMapVM(anim.fileName).await()
            list.add(Triple(anim, bitmap, i.toLong()+1))
        }
        return list
    }

    private fun getBitMapVM(fileName: String) = viewModelScope.async(exceptionHandler) {
        getBitMap(fileName, application)
    }

    fun deleteAnimation(animation: Animation?) {
        animation?.let {
            viewModelScope.launch(coroutineContext) {
                val createAt = animation.createAt
                repository.deleteAnim(createAt)
                deleteFile(application, animation.fileName)
            }
        }
        pathList.clear()
        _bitmapImage.value = null
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

    fun changeSliderState(flag: Boolean) {
        _sliderState.update { flag }
    }

    fun changeActiveAnim(animation: Animation?) {
        _activeAnim.update { animation }
        viewModelScope.launch(coroutineContext) {
            animation?.fileName?.let {
                _bitmapImage.update { getBitMap(animation.fileName, application) }
            }
        }
    }

    companion object {
        const val WIDTH_PEN = 1f
        const val WIDTH_BRUSH = 50f
        const val WIDTH_ERASER = 40f
    }
}