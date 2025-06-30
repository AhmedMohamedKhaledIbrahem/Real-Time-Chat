package com.example.realtimechatapp.core.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    data class StringResource(@StringRes val id: Int, val args: List<Any> = emptyList()) : UiText()

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> LocalContext.current.getString(id, *args.toTypedArray())
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(id, *args.toTypedArray())
        }
    }

    companion object {
        fun from(value: String): UiText = DynamicString(value)
        fun from(@StringRes id: Int, vararg args: Any): UiText =
            StringResource(id, args = args.toList())
    }


}