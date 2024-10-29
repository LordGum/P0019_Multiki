package com.example.multiki.presentation

import android.os.Bundle
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.multiki.R
import com.example.multiki.domain.Tool
import com.example.multiki.presentation.components.BottomInstruments
import com.example.multiki.presentation.components.SimplePalette
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

                val systemUiController = rememberSystemUiController()
                systemUiController.setStatusBarColor(Black)
                systemUiController.setNavigationBarColor(Black)

                Column(
                    modifier = Modifier
                        .background(Black)
                        .fillMaxSize()
                        .padding(top = 124.dp)
                ) {
                    AppCanvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.85f)
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        color = state.activeColor
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
                if(
                    state.activeTool == Tool.COLOR_SIMPLE ||
                    state.activeTool == Tool.COLOR_HARD
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
            }
        }
    }
}

@Composable
fun AppCanvas(
    modifier: Modifier = Modifier,
    color: Color
) {
    Box {
        Image(
            modifier = modifier,
            painter = painterResource(id = R.drawable.canvas_back),
            contentDescription = stringResource(R.string.canvas_desc),
            contentScale = ContentScale.Crop
        )

        DrawCanvas(modifier = modifier, color = color)
    }
}