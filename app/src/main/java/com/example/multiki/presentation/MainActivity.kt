package com.example.multiki.presentation

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.multiki.R
import com.example.multiki.domain.Animation
import com.example.multiki.domain.PathData
import com.example.multiki.domain.Tool
import com.example.multiki.presentation.components.AnimationSlider
import com.example.multiki.presentation.components.BottomInstruments
import com.example.multiki.presentation.components.DrawCanvas
import com.example.multiki.presentation.components.PenWidthLine
import com.example.multiki.presentation.components.SimplePalette
import com.example.multiki.presentation.components.TopInstruments
import com.example.multiki.presentation.components.pointerInteropFilterNative
import com.example.multiki.ui.theme.Black
import com.example.multiki.ui.theme.Blue
import com.example.multiki.ui.theme.MultikiTheme
import com.example.multiki.ui.theme.White
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MultikiTheme {
                val vm = ViewModelProvider(this)[MainViewModel::class.java]
                val state = vm.screenState.collectAsState().value as MainScreenState.Value
                val pathData = vm.pathData.collectAsState()
                val pathList = vm.pathList
                val pathForwardList = vm.pathForwardList.collectAsState()
                val paletteState = vm.paletteState.collectAsState()
                val widthLineState = vm.widthLineState.collectAsState()
                val sliderState = vm.sliderState.collectAsState()
                val saveFlag = vm.saveFlag.collectAsState()

                val bitmapImage = vm.bitmapImage.collectAsState()

                val systemUiController = rememberSystemUiController()
                systemUiController.setStatusBarColor(Black)
                systemUiController.setNavigationBarColor(Black)

                Column(
                    modifier = Modifier
                        .background(Black)
                        .fillMaxSize()
                        .padding(top = 34.dp)
                        .pointerInteropFilterNative {
                            vm.onTapFilter()
                            false
                        }
                ) {
                    TopInstruments(
                        backIconEnable = pathList.isNotEmpty(),
                        forwardIconEnable = pathForwardList.value.isNotEmpty(),
                        onBackClick = { vm.removeLastPath() },
                        onForwardClick = { vm.returnLastPath() },
                        onAddNewCanvas = { vm.changeSaveFlag(true) },
                        onDeleteAnimation = {
//                            vm.deleteAnimation()
                            vm.loadAnimation("new_haha_file", application)
                            Log.d("lama", "load anim")
                        },
                        onLayersClick = { vm.changeSliderState() }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AppCanvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.85f)
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        pathData = pathData,
                        pathList = pathList,
                        onAddPath = { vm.addPath(it) },
                        saveFlag = saveFlag.value,
                        onSaveClick = { vm.addAnimation(it) },
                        imageBitmap = bitmapImage.value
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    BottomInstruments(
                        activeTool = state.activeTool,
                        activeColor = state.activeColor,
                        onPenClick = { vm.changeTool(Tool.PEN) },
                        onBrushClick = { vm.changeTool(Tool.BRUSH) },
                        onEraserClick = { vm.changeTool(Tool.ERASER) },
                        onFigureChoice = { vm.changeTool(Tool.FIGURE_CHOICE) },
                        onColorSimpleClick = { vm.changeTool(Tool.COLOR_SIMPLE) }
                    )
                }
                if (
                    paletteState.value &&
                    (state.activeTool == Tool.COLOR_SIMPLE ||
                            state.activeTool == Tool.COLOR_HARD)
                ) {
                    SimplePalette(
                        activeTool = state.activeTool,
                        activeColor = state.activeColor,
                        onHardPalette = { vm.changeTool(Tool.COLOR_HARD) },
                        onColorWhiteClick = { vm.changeColor(White) },
                        onColorRedClick = { vm.changeColor(Red) },
                        onColorBlackClick = { vm.changeColor(Black) },
                        onColorBlueClick = { vm.changeColor(Blue) }
                    )
                }

                if (widthLineState.value) {
                    PenWidthLine(
                        sliderPosition = pathData.value.lineWidth,
                        onLineWidthChange = { lineWidth ->
                            vm.changeLineWidth(lineWidth)
                        }
                    )
                }

                if(sliderState.value) {
                    val anim = Animation(
                        createAt = 0L,
                        fileName = "new_haha_file"
                    )
                    AnimationSlider(
                        list = listOf(anim),
                        viewModel = vm
                    )
                }

            }
        }
    }
}

@Composable
fun AppCanvas(
    modifier: Modifier = Modifier,
    pathData: State<PathData>,
    pathList: SnapshotStateList<PathData>,
    onAddPath: (PathData) -> Unit,
    saveFlag: Boolean,
    onSaveClick: (ImageBitmap) -> Unit,
    imageBitmap: Bitmap?
) {
    Box {
        imageBitmap?.let {
            Image(
                modifier = modifier,
                bitmap = imageBitmap.asImageBitmap(),
                contentDescription = stringResource(R.string.canvas_desc),
                contentScale = ContentScale.None,
                alignment = Alignment.TopStart
            )
        }

        DrawCanvas(
            modifier = modifier,
            pathData = pathData,
            pathList = pathList,
            onAddPath = onAddPath,
            saveFlag = saveFlag,
            onSaveClick = onSaveClick,
            imageBitmap = imageBitmap
        )
    }
}
