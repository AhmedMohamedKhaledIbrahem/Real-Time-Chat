package com.example.realtimechatapp.features.authentication.presentation

import com.example.realtimechatapp.R
import com.example.realtimechatapp.core.error.AuthDomainError
import com.example.realtimechatapp.core.ui_text.UiText

fun AuthDomainError.asUiText(): UiText {
    return when (this) {
        // Local Errors
        AuthDomainError.Local.DATABASE_ERROR ->
            UiText.StringResource(R.string.error_database_error)

        AuthDomainError.Local.READ_FAILED ->
            UiText.StringResource(R.string.error_read_failed)

        AuthDomainError.Local.UNKNOWN ->
            UiText.StringResource(R.string.error_local_unknown)

        // Network Errors
        AuthDomainError.Network.AUTH_FAILED ->
            UiText.StringResource(R.string.error_auth_failed)

        AuthDomainError.Network.USER_NOT_FOUND ->
            UiText.StringResource(R.string.error_user_not_found)

        AuthDomainError.Network.USER_ALREADY_EXISTS ->
            UiText.StringResource(R.string.error_user_already_exists)

        AuthDomainError.Network.NO_USER_LOGGED_IN ->
            UiText.StringResource(R.string.error_no_user_logged_in)

        AuthDomainError.Network.EMAIL_NOT_VERIFIED ->
            UiText.StringResource(R.string.error_email_not_verified)

        AuthDomainError.Network.PERMISSION_DENIED ->
            UiText.StringResource(R.string.error_permission_denied)

        AuthDomainError.Network.NETWORK_UNAVAILABLE ->
            UiText.StringResource(R.string.error_network_unavailable)

        AuthDomainError.Network.INVALID_SIGN_UP_PARAMS ->
            UiText.StringResource(R.string.error_invalid_sign_up_params)

        AuthDomainError.Network.INVALID_LOGIN_PARAMS ->
            UiText.StringResource(R.string.error_invalid_login_params)

        AuthDomainError.Network.INVALID_EMAIL ->
            UiText.StringResource(R.string.error_invalid_email)

        AuthDomainError.Network.TIMEOUT ->
            UiText.StringResource(R.string.error_timeout)

        AuthDomainError.Network.UNKNOWN ->
            UiText.StringResource(R.string.error_network_unknown)
    }
}
