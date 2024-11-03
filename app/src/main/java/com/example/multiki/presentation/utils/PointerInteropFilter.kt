package com.example.multiki.presentation.utils

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.pointerInteropFilterNative(onTouchEvent: () -> Boolean): Modifier =
    pointerInteropFilter(
        requestDisallowInterceptTouchEvent = null
    ) { onTouchEvent() }