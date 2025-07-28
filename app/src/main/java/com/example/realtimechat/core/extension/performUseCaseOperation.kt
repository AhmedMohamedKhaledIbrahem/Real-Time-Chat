package com.example.realtimechat.core.extension

import androidx.lifecycle.ViewModel
import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.core.utils.RootError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

inline fun <D, reified E : RootError> ViewModel.performUseCaseOperation(
    crossinline useCase: suspend () -> Result<D, E>,
    crossinline onSuccess: suspend (D) -> Unit,
    crossinline onError: suspend (Result.Error<*, E>) -> Unit,
    scope: CoroutineScope
) {
    scope.launch {
        val result = useCase()
        when (result) {
            is Result.Success -> onSuccess(result.data)
            is Result.Error -> onError(result)
        }
    }
}

inline fun <D, reified E : RootError> ViewModel.performUseCaseOperation(
    crossinline useCase: () -> Result<D, E>,
    crossinline onSuccess: (D) -> Unit,
    crossinline onError: (Result.Error<*, E>) -> Unit,
) {
    when (val result = useCase()) {
        is Result.Success -> onSuccess(result.data)
        is Result.Error -> onError(result)
    }
}
