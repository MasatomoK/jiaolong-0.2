package com.masatomo.jiaolong.sample.service.grpc

import com.masatomo.jiaolong.core.domain.values.IntegralId
import com.masatomo.jiaolong.sample.domain.User
import com.masatomo.jiaolong.sample.grpc.UserServiceGrpcKt
import com.masatomo.jiaolong.sample.service.UserService
import io.grpc.ManagedChannel
import java.io.Closeable
import java.util.concurrent.TimeUnit

internal class UserServiceGrpcClientImpl internal constructor(
    private val channel: ManagedChannel
) : UserService, Closeable {

    private val stub = UserServiceGrpcKt.UserServiceCoroutineStub(channel)

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }

    override suspend fun register(user: User): IntegralId<User> = registerRequestFrom(user)
        .let { stub.register(it) }
        .toId()

    override suspend fun find(id: IntegralId<User>) = findRequestFrom(id)
        .let { stub.find(it) }
        .toUser()
}
