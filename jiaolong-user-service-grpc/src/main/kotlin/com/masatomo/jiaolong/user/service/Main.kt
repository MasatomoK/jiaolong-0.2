package com.masatomo.jiaolong.user.service

import com.masatomo.jiaolong.core.server.jiaolongApplication
import com.masatomo.jiaolong.core.service.grpc.GRpc
import com.masatomo.jiaolong.user.repository.userRepositoryModule
import com.masatomo.jiaolong.user.service.grpc.UserServiceServer
import io.ktor.server.engine.embeddedServer

fun main() {
    jiaolongApplication {
        koin {
            modules(
                userServiceModule,
                userRepositoryModule,
            )
        }
        ktor {
            embeddedServer(
                GRpc,
                port = 50051,
                configure = {
                    service { UserServiceServer(get(), it) }
                },
                module = {}
            )
        }
    }.start()
}