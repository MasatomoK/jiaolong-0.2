package com.masatomo.jiaolong.admin.server

import com.masatomo.jiaolong.admin.server.application.module
import com.masatomo.jiaolong.core.server.jiaolongApplication
import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer

fun main() {
    jiaolongApplication {
        koin { }
        ktor {
            embeddedServer(
                CIO,
                port = 8081,
                configure = {},
                module = Application::module
            )
        }
    }.start()
}
