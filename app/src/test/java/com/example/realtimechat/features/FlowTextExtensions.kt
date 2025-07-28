package com.example.realtimechat.features

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

fun <T> CoroutineScope.activeTestFLow(stateFlow: StateFlow<T>) = launch {
    stateFlow.collect {}
}

suspend fun <T> collectEvent(
    flow: Flow<T>,
    events: MutableList<T>,
    countEvent: Int = 1,
): List<T> {
    flow.take(countEvent).collect {
        events.add(it)
    }
    return events

}