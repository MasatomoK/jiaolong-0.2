package com.masatomo.jiaolong.user.service.grpc

import com.masatomo.jiaolong.user.service.UserService
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.koin.dsl.module

val userServiceGrpcClientModule = module {
    single<ManagedChannel> {
        ManagedChannelBuilder
            .forAddress("localhost", 50051)
            .usePlaintext()
            .build()
    }
    single<UserService> { UserServiceGrpcClientImpl(get()) }
}
