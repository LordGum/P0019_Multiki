package com.example.multiki.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import com.example.multiki.R
import com.example.multiki.domain.PathData

@Composable
fun DrawCanvas(
    modifier: Modifier,
    pathData: State<PathData>,
    pathList: State<List<PathData>>,
    onAddPath: (PathData) -> Unit
) {
    var tempPath = Path()

    val imageBrush = ShaderBrush(
        ImageShader(
            image = ImageBitmap.imageResource(id = R.drawable.canvas_back)
        )
    )

    Canvas(
        modifier = modifier
            .pointerInput(true) {
                detectDragGestures(
                    onDragStart = { tempPath = Path() },
                    onDragEnd = {
                        val newPath = pathData.value.copy(path = tempPath)
                        onAddPath(newPath)
                    }
                ) { change, dragAmount ->
                    tempPath.moveTo(
                        change.position.x - dragAmount.x,
                        change.position.y - dragAmount.y
                    )
                    tempPath.lineTo(
                        change.position.x,
                        change.position.y
                    )
                    val newPath = pathData.value.copy(path = tempPath)
                    onAddPath(newPath)
                }
            }
            .pointerInput(true) {
                detectTapGestures(
                    onTap = { set ->
                        tempPath = Path()
                        tempPath.moveTo(
                            set.x,
                            set.y,
                        )
                        tempPath.lineTo(
                            set.x,
                            set.y
                        )
                        val newPath = pathData.value.copy(path = tempPath)
                        onAddPath(newPath)
                    }
                )
            }
    ) {
        pathList.value.forEach { pathData ->
            drawPath(
                path = pathData.path,
                color = pathData.color,
                style = Stroke(
                    pathData.lineWidth,
                    cap = StrokeCap.Round
                )
            )
            if (pathData.isEraser) {
                drawPath(
                    path = pathData.path,
                    brush = imageBrush,
                    style = Stroke(
                        pathData.lineWidth,
                        cap = StrokeCap.Round
                    )
                )
            }
        }
    }
}