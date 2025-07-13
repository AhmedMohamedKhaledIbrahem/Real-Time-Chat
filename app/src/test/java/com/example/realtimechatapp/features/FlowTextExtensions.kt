package com.example.realtimechatapp.features

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

fun <T>CoroutineScope.activeTestFLow(stateFlow: StateFlow<T>) = launch {
    stateFlow.collect {}
}