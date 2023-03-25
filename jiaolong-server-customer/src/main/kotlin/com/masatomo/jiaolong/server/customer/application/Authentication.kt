package com.masatomo.jiaolong.server.customer.application

import io.ktor.server.auth.AuthenticationConfig

internal fun AuthenticationConfig.configure() {
    provider {
        authenticate {
        }
    }
}
