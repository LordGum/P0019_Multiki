package com.example.multiki.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import com.example.multiki.R
import com.example.multiki.ui.theme.Black
import com.example.multiki.ui.theme.White

@Composable
fun DropSettingsMenu(
    onDeleteClick: () -> Unit,
    onAllDeleteClick: () -> Unit,
) {
    val isClicked = remember { mutableStateOf(false) }
    val widthSize = animateDpAsState(
        targetValue = if (isClicked.value) 90.dp else 35.dp,
        label = "heightSize"
    )

    Row(
        modifier = Modifier.padding(top = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        FloatingActionButton(
            onClick = {
                isClicked.value = !isClicked.value
            },
            elevation = FloatingActionButtonDefaults.elevation(),
            containerColor = Black,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_delete),
                tint = Color.White,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
        }

        AnimatedVisibility(
            visible = isClicked.value,
            enter = expandHorizontally(),
            exit = shrinkHorizontally()
        ) {
            Row(
                modifier = Modifier.width(widthSize.value),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                CustomButton(
                    onButtonClick = { onDeleteClick() },
                    id = R.drawable.ic_delete,
                    modifier = Modifier.size(32.dp),
                    tint = White
                )
                Spacer(modifier = Modifier.height(5.dp))
                CustomButton(
                    onButtonClick = { onAllDeleteClick() },
                    id = R.drawable.ic_delete,
                    modifier = Modifier.size(32.dp),
                    tint = Red
                )
            }
        }
    }
}

@Composable
fun CustomButton(
    onButtonClick: () -> Unit,
    id: Int,
    modifier: Modifier,
    tint: Color
) {
    Surface(
        shape = CircleShape,
        border = BorderStroke(1.dp, Gray),
        modifier = modifier.size(40.dp)
    ) {
        IconButton(
            onClick = onButtonClick,
            modifier = Modifier
                .size(32.dp)
                .background(color = Black)
                .clearAndSetSemantics { }
        ) {
            Icon(
                painter = painterResource(id),
                tint = tint,
                contentDescription = null
            )
        }
    }
}