package com.example.realtimechat.core.shared_preference

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.realtimechat.core.logger.Logger

interface RealTimeChatSharedPreference {
    fun saveToken(token: String)
    fun getToken(): String?
    fun clearToken()
    fun saveReceiverToken(token: String)
    fun getReceiverToken(): String?
    fun clearReceiverToken()

    fun saveEmail(email: String)
    fun getEmail(): String?
    fun clearEmail()

    fun saveChannelUid(channelUid: String)
    fun getChannelUid(): String?
    fun clearChannelUid()

    fun saveReceiverUid(uid: String)
    fun getReceiverUid(): String?
    fun clearReceiverUid()
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

    override fun saveReceiverToken(token: String) {
        try {
            sharedPreference.edit { putString(RECEIVER_TOKEN, token) }
        } catch (e: Exception) {
            e(e, ERROR_SAVING_TOKEN)
        }
    }

    override fun getReceiverToken(): String? {
        return try {
            sharedPreference.getString(RECEIVER_TOKEN, null)
        } catch (e: Exception) {
            e(e, ERROR_GETTING_TOKEN)
            null
        }
    }

    override fun clearReceiverToken() {
        try {
            sharedPreference.edit { remove(RECEIVER_TOKEN) }
        } catch (e: Exception) {
            e(e, ERROR_CLEARING_TOKEN)
        }
    }

    override fun saveEmail(email: String) {
        try {
            sharedPreference.edit { putString(EMAIL, email) }
        } catch (e: Exception) {
            e(e, ERROR_SAVING_EMAIL)
        }
    }

    override fun getEmail(): String? {
        return try {
            sharedPreference.getString(EMAIL, null)
        } catch (e: Exception) {
            e(e, ERROR_GETTING_EMAIL)
            null
        }
    }

    override fun clearEmail() {
        try {
            sharedPreference.edit { remove(EMAIL) }
        } catch (e: Exception) {
            e(e, ERROR_CLEARING_EMAIL)
        }
    }

    override fun saveChannelUid(channelUid: String) {
        try {
            sharedPreference.edit { putString(CHANNEL_UID, channelUid) }
        } catch (e: Exception) {
            e(e, ERROR_SAVING_EMAIL)
        }
    }

    override fun getChannelUid(): String? {
        return try {
            sharedPreference.getString(CHANNEL_UID, null)
        } catch (e: Exception) {
            e(e, ERROR_GETTING_EMAIL)
            null
        }
    }

    override fun clearChannelUid() {
        try {
            sharedPreference.edit { remove(CHANNEL_UID) }
        } catch (e: Exception) {
            e(e, ERROR_CLEARING_EMAIL)
        }
    }

    override fun saveReceiverUid(uid: String) {
        try {
            sharedPreference.edit { putString(RECEIVER_UID, uid) }
        } catch (e: Exception) {
            e(e, ERROR_CLEARING_EMAIL)
        }
    }

    override fun getReceiverUid(): String? {
        return try {
            sharedPreference.getString(RECEIVER_UID, null)
        } catch (e: Exception) {
            e(e, ERROR_CLEARING_EMAIL)
            null
        }
    }

    override fun clearReceiverUid() {
        try {
            sharedPreference.edit { remove(RECEIVER_UID) }
        } catch (e: Exception) {
            e(e, ERROR_CLEARING_EMAIL)
        }
    }

    companion object {
        private const val TOKEN = "token"
        private const val RECEIVER_TOKEN = "receiver_token"
        private const val EMAIL = "email"
        private const val CHANNEL_UID = "channel_uid"
        private const val RECEIVER_UID = "receiver_uid"
        private const val ERROR_SAVING_TOKEN = "Error saving token"
        private const val ERROR_GETTING_TOKEN = "Error getting token"
        private const val ERROR_CLEARING_TOKEN = "Error clearing token"

        private const val ERROR_SAVING_EMAIL = "Error saving email"
        private const val ERROR_GETTING_EMAIL = "Error getting email"
        private const val ERROR_CLEARING_EMAIL = "Error clearing email"
    }

}