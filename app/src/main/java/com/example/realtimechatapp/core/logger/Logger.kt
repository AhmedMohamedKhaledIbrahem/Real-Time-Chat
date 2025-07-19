package com.example.realtimechatapp.core.logger

import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

interface Logger {
    fun d(message: String)
    fun e(throwable: Throwable, message: String)
}

class ReporterLogger : Logger {
    override fun d(message: String) {
        Timber.d(message)
    }

    override fun e(throwable: Throwable, message: String) {
        Timber.e(throwable, message)
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }

}