package com.masatomo.jiaolong.user.service.grpc

import com.masatomo.jiaolong.core.repository.suspendTransaction
import com.masatomo.jiaolong.core.service.grpc.globalErrorHandler
import com.masatomo.jiaolong.user.grpc.UserServiceGrpcKt
import com.masatomo.jiaolong.user.grpc.UserServiceOuterClass
import com.masatomo.jiaolong.user.service.UserService
import com.masatomo.jiaolong.user.service.UserTransactionScope
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


internal class UserServiceServer(
    private val delegation: UserService,
    coroutineContext: CoroutineContext = EmptyCoroutineContext
) : UserServiceGrpcKt.UserServiceCoroutineImplBase(coroutineContext) {
    override suspend fun register(request: UserServiceOuterClass.RegisterRequest) = globalErrorHandler {
        suspendTransaction(UserTransactionScope) {
            delegation.register(request.toUser())
                .let { registerResponseFrom(it) }
        }
    }

    override suspend fun find(request: UserServiceOuterClass.FindRequest) = globalErrorHandler {
        suspendTransaction(UserTransactionScope) {
            delegation.find(request.toId())
                .let { findResponseFrom(it) }
        }
    }
}
