package com.example.multiki.presentation.components

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.example.multiki.domain.PathData

fun drawToBitmap(
    pathList: SnapshotStateList<PathData>,
    eraserBrush: ShaderBrush,
    bitmapBrush: ShaderBrush,
    height: Float,
    width: Float
): ImageBitmap {

    val drawScope = CanvasDrawScope()
    val size = Size(width, height)
    val bitmap = ImageBitmap(
        size.width.toInt(),
        size.height.toInt(),
    )
    val canvas = Canvas(bitmap)

    drawScope.draw(
        density = Density(1f),
        layoutDirection = LayoutDirection.Ltr,
        canvas = canvas,
        size = size,
    ) {
        drawRect(
            size = size,
            brush = bitmapBrush
        )
        pathList.forEach { pathData ->
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
                    brush = eraserBrush,
                    style = Stroke(
                        pathData.lineWidth,
                        cap = StrokeCap.Round
                    )
                )
            }
            Log.d("lama", "draw path")
        }
    }

    return bitmap
}