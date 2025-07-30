package com.example.realtimechat.features.authentication.presentation

import com.example.realtimechat.R
import com.example.realtimechat.core.error.DomainError
import com.example.realtimechat.core.ui_text.UiText
import com.example.realtimechat.core.ui_text.UiText.StringResource

fun DomainError.asUiText(): UiText {
    return when (this) {
        // Local Errors
        DomainError.Local.DATABASE_ERROR ->
            StringResource(R.string.error_database_error)

        DomainError.Local.READ_FAILED ->
            StringResource(R.string.error_read_failed)

        DomainError.Local.UNKNOWN ->
            StringResource(R.string.error_local_unknown)

        // Network Errors
        DomainError.Network.AUTH_FAILED ->
            StringResource(R.string.error_auth_failed)

        DomainError.Network.USER_NOT_FOUND ->
            StringResource(R.string.error_user_not_found)

        DomainError.Network.USER_ALREADY_EXISTS ->
            StringResource(R.string.error_user_already_exists)

        DomainError.Network.NO_USER_LOGGED_IN ->
            StringResource(R.string.error_no_user_logged_in)

        DomainError.Network.EMAIL_NOT_VERIFIED ->
            StringResource(R.string.error_email_not_verified)

        DomainError.Network.PERMISSION_DENIED ->
            StringResource(R.string.error_permission_denied)

        DomainError.Network.NETWORK_UNAVAILABLE ->
            StringResource(R.string.error_network_unavailable)

        DomainError.Network.INVALID_SIGN_UP_PARAMS ->
            StringResource(R.string.error_invalid_sign_up_params)

        DomainError.Network.INVALID_LOGIN_PARAMS ->
            StringResource(R.string.error_invalid_login_params)

        DomainError.Network.INVALID_EMAIL ->
            StringResource(R.string.error_invalid_email)

        DomainError.Network.TIMEOUT ->
            StringResource(R.string.error_timeout)

        DomainError.Network.UNKNOWN ->
            StringResource(R.string.error_network_unknown)

        DomainError.Network.NO_USER_DATA_FOUND ->
            StringResource(R.string.error_no_user_data_found)

        DomainError.Name.INVALID_NAME ->
            StringResource(R.string.error_invalid_name)

        DomainError.Email.INVALID_EMAIL ->
            StringResource(R.string.error_invalid_email)

        DomainError.Phone.INVALID_PHONE ->
            StringResource(R.string.error_invalid_phone)

        DomainError.Password.TOO_SHORT ->
            StringResource(R.string.error_password_too_short)

        DomainError.Password.NO_DIGIT ->
            StringResource(R.string.error_password_no_digit)

        DomainError.Password.NO_SPECIAL_CHARACTER ->
            StringResource(R.string.error_password_no_special_character)

        DomainError.Password.NO_UPPERCASE ->
            StringResource(R.string.error_password_no_uppercase)

        DomainError.Password.NO_LOWERCASE ->
            StringResource(R.string.error_password_no_lowercase)


    }
}
