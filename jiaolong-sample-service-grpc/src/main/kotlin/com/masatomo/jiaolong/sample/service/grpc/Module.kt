package com.masatomo.jiaolong.sample.service.grpc

import com.masatomo.jiaolong.sample.service.UserService
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.koin.dsl.module

val sampleServiceGrpcClientModule = module {
    single<ManagedChannel> {
        ManagedChannelBuilder
            .forAddress("localhost", 50051)
            .usePlaintext()
            .build()
    }
    single<UserService> { UserServiceGrpcClientImpl(get()) }
}
