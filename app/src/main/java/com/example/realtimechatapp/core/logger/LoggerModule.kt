package com.example.realtimechatapp.core.logger

import org.koin.dsl.module

val loggerModule = module {
    single <Logger> { ReporterLogger() }
}