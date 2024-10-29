package com.example.multiki.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.example.multiki.domain.PathData

@Composable
fun DrawCanvas(
    modifier: Modifier,
    pathData: State<PathData>,
    pathList: State<List<PathData>>,
    onAddPath: (PathData) -> Unit
) {
    var tempPath = Path()
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
        }
    }
}