package com.masatomo.jiaolong.user.service

import com.masatomo.jiaolong.core.service.grpc.GRpc
import com.masatomo.jiaolong.user.repository.userRepositoryModule
import io.ktor.server.engine.embeddedServer
import org.koin.dsl.koinApplication

fun main() {
    koinApplication {
        modules(
            userServiceModule,
            userRepositoryModule
        )
    }.run {
        embeddedServer(
            GRpc,
            port = 50051,
            configure = {
                service { UserServiceServer(this@run.koin.get(), it) }
            },
            module = {}
        ).start(wait = true)
    }
}
