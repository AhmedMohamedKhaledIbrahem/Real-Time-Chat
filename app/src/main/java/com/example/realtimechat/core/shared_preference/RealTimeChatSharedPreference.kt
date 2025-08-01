package com.example.realtimechat.core.shared_preference

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.realtimechat.core.logger.Logger

interface RealTimeChatSharedPreference {
    fun saveToken(token: String)
    fun getToken(): String?
    fun clearToken()
}

class RealTimeChatSharedPreferenceImpl(
    private val sharedPreference: SharedPreferences,
    logger: Logger
) : RealTimeChatSharedPreference, Logger by logger {
    override fun saveToken(token: String) {
        try {
            sharedPreference.edit { putString(TOKEN, token) }
        } catch (e: Exception) {
            e(e, ERROR_SAVING_TOKEN)
        }
    }

    override fun getToken(): String? {
        return try {
            sharedPreference.getString(TOKEN, null)
        } catch (e: Exception) {
            e(e, ERROR_GETTING_TOKEN)
            null
        }
    }

    override fun clearToken() {
        try {
            sharedPreference.edit { remove(TOKEN) }
        } catch (e: Exception) {
            e(e, ERROR_CLEARING_TOKEN)
        }
    }

    companion object {
        const val TOKEN = "token"
        const val ERROR_SAVING_TOKEN = "Error saving token"
        const val ERROR_GETTING_TOKEN = "Error getting token"
        const val ERROR_CLEARING_TOKEN = "Error clearing token"
    }

}