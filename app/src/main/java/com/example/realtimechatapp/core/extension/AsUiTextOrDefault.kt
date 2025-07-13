package com.example.realtimechatapp.core.extension

import com.example.realtimechatapp.core.error.AuthDomainError
import com.example.realtimechatapp.core.ui_text.UiText
import com.example.realtimechatapp.core.utils.Result
import com.example.realtimechatapp.core.utils.RootError
import com.example.realtimechatapp.features.authentication.presentation.asUiText


inline fun <reified E : RootError> Result.Error<*, E>.asUiTextOrDefault(
    default: UiText = UiText.DynamicString(""),
): UiText {
    return when (val err = this.error) {
        is AuthDomainError -> err.asUiText()
        else -> default
    }
}