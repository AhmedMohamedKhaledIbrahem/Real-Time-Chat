package com.example.realtimechat.core.module

import com.example.internet_connection_monitor.network.InternetConnectionMonitor
import com.example.internet_connection_monitor.network.InternetConnectionMonitorImpl
import org.koin.dsl.module

val networkModule = module {
    single<InternetConnectionMonitor> { InternetConnectionMonitorImpl(get()) }
}