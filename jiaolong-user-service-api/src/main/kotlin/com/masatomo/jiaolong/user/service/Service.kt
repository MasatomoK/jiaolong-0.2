package com.masatomo.jiaolong.user.service

import com.masatomo.jiaolong.core.common.IntegralId
import com.masatomo.jiaolong.user.domain.User
import com.masatomo.jiaolong.user.grpc.registerRequest
import com.masatomo.jiaolong.user.grpc.user
import com.masatomo.jiaolong.user.service.grpc.UserServiceClient
import io.grpc.ManagedChannelBuilder

interface UserService {
    suspend fun register(user: User): IntegralId<User>
    suspend fun find(id: IntegralId<User>): User?
}

suspend fun main() {
    val port = 50051

    val channel = ManagedChannelBuilder
        .forAddress("localhost", port)
        .usePlaintext()
        .build()

    UserServiceClient(channel).use {
        it.register(registerRequest {
            this.user = user {
                this.name = "name"
                this.password = "password"
            }
        })
    }
}
