package com.example.multiki.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.multiki.R
import com.example.multiki.domain.Tool
import com.example.multiki.ui.theme.Blue
import com.example.multiki.ui.theme.LimeGreen
import com.example.multiki.ui.theme.White

@Composable
fun BottomInstruments(
    activeTool: Tool,
    activeColor: Color = Blue,
    onPenClick: () -> Unit,
    onBrushClick: () -> Unit,
    onEraserClick: () -> Unit,
    onFigureChoice: () -> Unit,
    onColorSimpleClick: () -> Unit,
    onColorHardClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 50.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPenClick) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = R.drawable.ic_pen),
                contentDescription = stringResource(R.string.pen_tool),
                tint = if(activeTool == Tool.PEN) LimeGreen else White
            )
        }
        IconButton(onClick = onBrushClick) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = R.drawable.ic_brush),
                contentDescription = stringResource(R.string.brush_tool),
                tint = if(activeTool == Tool.BRUSH) LimeGreen else White
            )
        }
        IconButton(onClick = onEraserClick) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = R.drawable.ic_eraser),
                contentDescription = stringResource(R.string.eraser_tool),
                tint = if(activeTool == Tool.ERASER) LimeGreen else White
            )
        }
        IconButton(onClick = onFigureChoice) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = R.drawable.ic_figure),
                contentDescription = stringResource(R.string.figure_choice_tool),
                tint = if(activeTool == Tool.FIGURE_CHOICE) LimeGreen else White
            )
        }
        IconButton(onClick = onColorSimpleClick) {
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .border(
                        width = 2.dp,
                        color = if(activeTool == Tool.COLOR_SIMPLE) LimeGreen else White,
                        shape = CircleShape
                    ),
                painter = painterResource(id = R.drawable.ic_color_choice),
                contentDescription = stringResource(R.string.color_simple_tool),
                tint = activeColor
            )
        }
    }
}
