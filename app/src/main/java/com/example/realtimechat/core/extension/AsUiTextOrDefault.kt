package com.example.realtimechat.core.extension

import com.example.realtimechat.core.error.AuthDomainError
import com.example.realtimechat.core.ui_text.UiText
import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.core.utils.RootError
import com.example.realtimechat.features.authentication.presentation.asUiText


inline fun <reified E : RootError> Result.Error<*, E>.asUiTextOrDefault(
    default: UiText = UiText.DynamicString(""),
): UiText {
    return when (val err = this.error) {
        is AuthDomainError -> err.asUiText()
        else -> default
    }
}