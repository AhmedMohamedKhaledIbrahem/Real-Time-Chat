package com.example.realtimechat.core.extension

import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.core.utils.RootError

inline fun <D, E : RootError, R> Result<D, E>.fold(
    onSuccess: (D) -> R,
    onError: (E) -> R
): R = when (this) {
    is Result.Success -> onSuccess(data)
    is Result.Error -> onError(error)
}