package com.example.multiki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorProperty
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.multiki.ui.theme.Black
import com.example.multiki.ui.theme.MultikiTheme
import com.example.multiki.ui.theme.White
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultikiTheme {
                val systemUiController = rememberSystemUiController()
                systemUiController.setStatusBarColor(Black)
                systemUiController.setNavigationBarColor(Black)

                AppCanvas()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppCanvas() {
    Box(
        modifier = Modifier
            .background(Black)
            .fillMaxSize()
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 124.dp)
                .padding(bottom = 150.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(20.dp)),
            painter = painterResource(id = R.drawable.canvas_back),
            contentDescription = stringResource(R.string.canvas_desc),
            contentScale = ContentScale.Crop
        )

        DrawCanvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 124.dp)
                .padding(bottom = 150.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(20.dp))
        )
    }
}