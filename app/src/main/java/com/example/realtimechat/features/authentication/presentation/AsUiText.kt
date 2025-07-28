package com.example.realtimechat.features.authentication.presentation

import com.example.realtimechat.R
import com.example.realtimechat.core.error.AuthDomainError
import com.example.realtimechat.core.ui_text.UiText
import com.example.realtimechat.core.ui_text.UiText.StringResource

fun AuthDomainError.asUiText(): UiText {
    return when (this) {
        // Local Errors
        AuthDomainError.Local.DATABASE_ERROR ->
            StringResource(R.string.error_database_error)

        AuthDomainError.Local.READ_FAILED ->
            StringResource(R.string.error_read_failed)

        AuthDomainError.Local.UNKNOWN ->
            StringResource(R.string.error_local_unknown)

        // Network Errors
        AuthDomainError.Network.AUTH_FAILED ->
            StringResource(R.string.error_auth_failed)

        AuthDomainError.Network.USER_NOT_FOUND ->
            StringResource(R.string.error_user_not_found)

        AuthDomainError.Network.USER_ALREADY_EXISTS ->
            StringResource(R.string.error_user_already_exists)

        AuthDomainError.Network.NO_USER_LOGGED_IN ->
            StringResource(R.string.error_no_user_logged_in)

        AuthDomainError.Network.EMAIL_NOT_VERIFIED ->
            StringResource(R.string.error_email_not_verified)

        AuthDomainError.Network.PERMISSION_DENIED ->
            StringResource(R.string.error_permission_denied)

        AuthDomainError.Network.NETWORK_UNAVAILABLE ->
            StringResource(R.string.error_network_unavailable)

        AuthDomainError.Network.INVALID_SIGN_UP_PARAMS ->
            StringResource(R.string.error_invalid_sign_up_params)

        AuthDomainError.Network.INVALID_LOGIN_PARAMS ->
            StringResource(R.string.error_invalid_login_params)

        AuthDomainError.Network.INVALID_EMAIL ->
            StringResource(R.string.error_invalid_email)

        AuthDomainError.Network.TIMEOUT ->
            StringResource(R.string.error_timeout)

        AuthDomainError.Network.UNKNOWN ->
            StringResource(R.string.error_network_unknown)

        AuthDomainError.Network.NO_USER_DATA_FOUND ->
            StringResource(R.string.error_no_user_data_found)

        AuthDomainError.Name.INVALID_NAME ->
            StringResource(R.string.error_invalid_name)

        AuthDomainError.Email.INVALID_EMAIL ->
            StringResource(R.string.error_invalid_email)

        AuthDomainError.Phone.INVALID_PHONE ->
            StringResource(R.string.error_invalid_phone)

        AuthDomainError.Password.TOO_SHORT ->
            StringResource(R.string.error_password_too_short)

        AuthDomainError.Password.NO_DIGIT ->
            StringResource(R.string.error_password_no_digit)

        AuthDomainError.Password.NO_SPECIAL_CHARACTER ->
            StringResource(R.string.error_password_no_special_character)

        AuthDomainError.Password.NO_UPPERCASE ->
            StringResource(R.string.error_password_no_uppercase)

        AuthDomainError.Password.NO_LOWERCASE ->
            StringResource(R.string.error_password_no_lowercase)


    }
}
