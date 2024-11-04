package com.example.multiki.presentation

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelProvider
import com.example.multiki.domain.Animation
import com.example.multiki.ui.theme.Black
import com.example.multiki.ui.theme.MultikiTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val systemUiController = rememberSystemUiController()
            systemUiController.setStatusBarColor(Black)
            systemUiController.setNavigationBarColor(Black)

            MultikiTheme {
                val vm = ViewModelProvider(this)[MainViewModel::class.java]

                val screenState = vm.screenState.collectAsState()
                val animList = vm.animList.collectAsState(initial = listOf())
                val listForSlider =
                    remember { mutableStateOf<List<Triple<Animation, Bitmap?, Long>>>(listOf()) }

                when (val currentScreenState = screenState.value) {
                    is MainScreenState.Loading -> LoadingScreen()
                    is MainScreenState.Video -> {
                        VideoScreen(
                            dataList = animList.value,
                            listAnim = listForSlider.value,
                            onBackClick = {
                                vm.changeScreenState(MainScreenState.Value())
                            }
                        )
                    }

                    is MainScreenState.Value -> {
                        SideEffect {
                            CoroutineScope(Dispatchers.IO).launch {
                                listForSlider.value = vm.loadAnimList(animList.value)
                            }
                        }
                        ValueScreen(
                            vm = vm,
                            animList = animList.value,
                            state = currentScreenState,
                            onRunClick = {
                                vm.changeScreenState(MainScreenState.Video)
                            }
                        )
                    }
                }
            }
        }
    }
}
