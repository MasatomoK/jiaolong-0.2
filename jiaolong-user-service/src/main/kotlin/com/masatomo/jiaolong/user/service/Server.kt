package com.masatomo.jiaolong.user.service

import com.masatomo.jiaolong.core.logging.getLogger
import com.masatomo.jiaolong.user.grpc.UserServiceGrpcKt
import com.masatomo.jiaolong.user.grpc.UserServiceOuterClass
import com.masatomo.jiaolong.user.grpc.findResponse
import com.masatomo.jiaolong.user.grpc.registerResponse
import com.masatomo.jiaolong.user.service.grpc.toDomain
import com.masatomo.jiaolong.user.service.grpc.toModel
import io.grpc.Status
import io.grpc.StatusException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


private val logger = getLogger()

class UserServiceServer(
    private val delegation: UserService,
    coroutineContext: CoroutineContext = EmptyCoroutineContext
) : UserServiceGrpcKt.UserServiceCoroutineImplBase(coroutineContext) {
    override suspend fun register(request: UserServiceOuterClass.RegisterRequest): UserServiceOuterClass.RegisterResponse = globalErrorHandler {
        delegation.register(request.user.toDomain())
            .toModel()
            .let {
                registerResponse {
                    id = it
                }
            }
    }

    override suspend fun find(request: UserServiceOuterClass.FindRequest): UserServiceOuterClass.FindResponse = globalErrorHandler {
        delegation.find(request.id.toDomain())
            ?.toModel()
            .let {
                findResponse {
                    if (it != null) user = it
                }
            }
    }
}

suspend fun <T> globalErrorHandler(main: suspend () -> T): T = try {
    main.invoke()
} catch (e: UnsupportedOperationException) {
    logger.error("Got unknown error.", e)
    throw StatusException(Status.UNIMPLEMENTED)
} catch (e: Exception) {
    logger.error("Got unknown error.", e)
    throw StatusException(Status.INTERNAL)
}
