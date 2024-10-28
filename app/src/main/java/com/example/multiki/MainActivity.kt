package com.example.multiki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.example.multiki.ui.theme.MultikiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultikiTheme {
                DrawCanvas()
            }
        }
    }
}

@Composable
fun DrawCanvas() {
    val tempPath = androidx.compose.ui.graphics.Path()
    val path = remember {
        mutableStateOf(androidx.compose.ui.graphics.Path())
    }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(true) {
                detectDragGestures { change, dragAmount ->
                    tempPath.moveTo(
                        change.position.x - dragAmount.x,
                        change.position.y - dragAmount.y
                    )
                    tempPath.lineTo(
                        change.position.x,
                        change.position.y
                    )

                    path.value = androidx.compose.ui.graphics
                        .Path()
                        .apply { addPath(tempPath) }
                }
            }
    ) {
        drawPath(
            path = path.value,
            color = Color.Red,
            style = Stroke(10f)
        )
    }
}

