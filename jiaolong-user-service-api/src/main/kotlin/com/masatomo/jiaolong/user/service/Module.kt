package com.masatomo.jiaolong.user.service

import com.masatomo.jiaolong.user.service.grpc.UserServiceClient
import com.masatomo.jiaolong.user.service.grpc.UserServiceGrpcImpl
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.koin.dsl.module

val userServiceClientModule = module {
    single<ManagedChannel> {
        ManagedChannelBuilder
            .forAddress("localhost", 50051)
            .usePlaintext()
            .build()
    }
    single<UserServiceClient> { UserServiceClient(get()) }
    single<UserService> { UserServiceGrpcImpl(get()) }
}
