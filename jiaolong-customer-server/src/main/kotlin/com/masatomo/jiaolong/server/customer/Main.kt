package com.masatomo.jiaolong.server.customer

import com.masatomo.jiaolong.core.server.jiaolongApplication
import com.masatomo.jiaolong.server.customer.application.module
import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer

fun main() {
    jiaolongApplication {
        koin { }
        ktor {
            embeddedServer(
                CIO,
                port = 8080,
                configure = {},
                module = Application::module
            )
        }
    }.start()
}
