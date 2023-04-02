package com.masatomo.jiaolong.user.service.grpc

import com.masatomo.jiaolong.user.grpc.UserServiceGrpcKt
import com.masatomo.jiaolong.user.grpc.UserServiceOuterClass
import io.grpc.ManagedChannel
import java.io.Closeable
import java.util.concurrent.TimeUnit

class UserServiceClient(private val channel: ManagedChannel) : Closeable {

    private val stub = UserServiceGrpcKt.UserServiceCoroutineStub(channel)

    suspend fun register(request: UserServiceOuterClass.RegisterRequest) = stub.register(request)
        .also { println("Received: $it") }

    suspend fun find(request: UserServiceOuterClass.FindRequest) = stub.find(request)
        .also { println("Received: $it") }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}
