package com.masatomo.jiaolong.core.server.config

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.callloging.CallLoggingConfig
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import org.slf4j.event.Level

fun Application.logging(configure: CallLoggingConfig.() -> Unit) {
    install(CallLogging) {
        configure()
    }
}

fun CallLoggingConfig.configureDefault() {
    level = Level.INFO
    filter { call -> call.request.path().startsWith("/") }
    format { call ->
        val path = call.request.path()
        val status = call.response.status()
        val httpMethod = call.request.httpMethod.value
        val userAgent = call.request.headers["User-Agent"]
        "path: $path, Status: $status, HTTP method: $httpMethod, User agent: $userAgent"
    }
}