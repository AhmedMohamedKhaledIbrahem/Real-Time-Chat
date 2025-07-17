package com.example.realtimechatapp.core.extension

import com.example.realtimechatapp.core.utils.Result
import com.example.realtimechatapp.core.utils.RootError

inline fun <D, E : RootError, R> Result<D, E>.fold(
    onSuccess: (D) -> R,
    onError: (E) -> R
): R = when (this) {
    is Result.Success -> onSuccess(data)
    is Result.Error -> onError(error)
}