package com.masatomo.jiaolong.server.customer

import com.masatomo.jiaolong.server.customer.application.module
import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer

fun main() {
    embeddedServer(CIO, port = 8080, module = Application::module)
        .start(wait = true)
}
