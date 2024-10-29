package com.example.multiki.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.multiki.R
import com.example.multiki.ui.theme.White

@Composable
fun TopInstruments(
    backIconEnable: Boolean,
    forwardIconEnable: Boolean,
    onBackClick: () -> Unit,
    onForwardClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackAndForward(
            backIconEnable = backIconEnable,
            forwardIconEnable = forwardIconEnable,
            onBackClick = onBackClick,
            onForwardClick = onForwardClick
        )
        FileInstruments()
        PlayerInstrument()
    }
}

@Composable
fun BackAndForward(
    backIconEnable: Boolean,
    forwardIconEnable: Boolean,
    onBackClick: () -> Unit,
    onForwardClick: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.Absolute.SpaceAround) {
        IconButton(
            onClick = onBackClick,
            enabled = backIconEnable
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = R.drawable.ic_back_arrow),
                contentDescription = null,
                tint = if(backIconEnable) White else White.copy(alpha = 0.3f)
            )
        }

        IconButton(
            onClick = onForwardClick,
            enabled = forwardIconEnable
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = R.drawable.ic_forward_arrow),
                contentDescription = null,
                tint = if(forwardIconEnable) White else White.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
fun FileInstruments() {
    Row(
        horizontalArrangement = Arrangement.Absolute.SpaceAround
    ) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = null,
                tint = White
            )
        }

        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = null,
                tint = White
            )
        }

        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = R.drawable.ic_layers),
                contentDescription = null,
                tint = White
            )
        }
    }
}

@Composable
fun PlayerInstrument() {
    IconButton(onClick = { /*TODO*/ }) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = R.drawable.ic_play),
            contentDescription = null,
            tint = White
        )
    }
}