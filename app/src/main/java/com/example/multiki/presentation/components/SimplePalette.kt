package com.example.multiki.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.multiki.R
import com.example.multiki.domain.Tool
import com.example.multiki.ui.theme.Black
import com.example.multiki.ui.theme.Blue
import com.example.multiki.ui.theme.BoardGrey
import com.example.multiki.ui.theme.LimeGreen
import com.example.multiki.ui.theme.White

@Composable
fun SimplePalette(
    activeTool: Tool,
    activeColor: Color,
    onHardPalette: () -> Unit,
    onColorWhiteClick: () -> Unit,
    onColorRedClick: () -> Unit,
    onColorBlackClick: () -> Unit,
    onColorBlueClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
            .padding(vertical = 16.dp)
            .padding(top = 670.dp)
            .background(Black.copy(alpha = 0.14f))
            .clip(RoundedCornerShape(4.dp))
            .border(
                width = 1.dp,
                color = BoardGrey,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        val isSimpleTool = activeTool == Tool.COLOR_SIMPLE
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = onHardPalette) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.ic_hard_palette),
                    contentDescription = stringResource(R.string.hard_palette_tool),
                    tint = if(activeTool == Tool.COLOR_HARD) LimeGreen else White
                )
            }
            IconButton(onClick = onColorWhiteClick) {
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .border(
                            width = 2.dp,
                            color = if (activeColor == White && isSimpleTool) LimeGreen else White,
                            shape = CircleShape
                        ),
                    painter = painterResource(id = R.drawable.ic_color_choice),
                    contentDescription = null,
                    tint = White
                )
            }
            IconButton(onClick = onColorRedClick) {
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .border(
                            width = 2.dp,
                            color = if (activeColor == Red && isSimpleTool) LimeGreen else White,
                            shape = CircleShape
                        ),
                    painter = painterResource(id = R.drawable.ic_color_choice),
                    contentDescription = null,
                    tint = Red
                )
            }
            IconButton(onClick = onColorBlackClick) {
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .border(
                            width = 2.dp,
                            color = if (activeColor == Black && isSimpleTool) LimeGreen else White,
                            shape = CircleShape
                        ),
                    painter = painterResource(id = R.drawable.ic_color_choice),
                    contentDescription = null,
                    tint = Black
                )
            }
            IconButton(onClick = onColorBlueClick) {
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .border(
                            width = 2.dp,
                            color = if (activeColor == Blue && isSimpleTool) LimeGreen else White,
                            shape = CircleShape
                        ),
                    painter = painterResource(id = R.drawable.ic_color_choice),
                    contentDescription = null,
                    tint = Blue
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun T() {
    SimplePalette(
        activeTool = Tool.COLOR_HARD,
        activeColor = Black,
        onHardPalette = {},
        onColorWhiteClick = {},
        onColorRedClick = {},
        onColorBlackClick = {},
        onColorBlueClick = {}
    )
}