package com.masatomo.jiaolong.core.server.config

import io.ktor.server.auth.AuthenticationConfig

fun AuthenticationConfig.configureDefault() {
    provider {
        authenticate {
        }
    }
}
