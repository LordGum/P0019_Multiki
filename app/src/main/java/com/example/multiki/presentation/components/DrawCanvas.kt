package com.example.multiki.presentation.components

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import com.example.multiki.R
import com.example.multiki.domain.PathData

@Composable
fun DrawCanvas(
    modifier: Modifier,
    pathData: State<PathData>,
    saveFlag: Boolean,
    pathList: SnapshotStateList<PathData>,
    onAddPath: (PathData) -> Unit,
    onSaveClick: (ImageBitmap) -> Unit,
    imageBitmap: Bitmap?
) {
    var tempPath = Path()
    val eraserBrush = ShaderBrush(
        ImageShader(ImageBitmap.imageResource(id = R.drawable.canvas_back))
    )
    val bitmapBrush = ShaderBrush(
        ImageShader(
            imageBitmap?.asImageBitmap() ?: ImageBitmap.imageResource(id = R.drawable.canvas_back)
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

                    if (pathList.size > 0) {
                        pathList.removeAt(pathList.size - 1)
                    }
                    val newPath = pathData.value.copy(path = tempPath)
                    onAddPath(newPath)
                }
            }
            .pointerInput(true) {
                detectTapGestures(
                    onTap = {
                        tempPath = Path()
                        tempPath.moveTo(it.x, it.y)
                        tempPath.lineTo(it.x, it.y)
                        val newPath = pathData.value.copy(path = tempPath)
                        onAddPath(newPath)
                    }
                )
            }
    ) {
        val image = drawToBitmap(
            pathList = pathList,
            eraserBrush = eraserBrush,
            bitmapBrush = bitmapBrush,
            height = this.size.height,
            width = this.size.width
        )
        drawImage(image)

        if (saveFlag) {
            onSaveClick(image)
        }
    }
}