package com.example.realtimechat.core.logger

import org.koin.dsl.module

val loggerModule = module {
    single <Logger> { ReporterLogger() }
}