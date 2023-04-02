package com.masatomo.jiaolong.user.service

import com.masatomo.jiaolong.user.grpc.UserServiceGrpcKt
import com.masatomo.jiaolong.user.grpc.UserServiceOuterClass
import com.masatomo.jiaolong.user.grpc.findResponse
import com.masatomo.jiaolong.user.grpc.registerResponse
import com.masatomo.jiaolong.user.service.grpc.toDomain
import com.masatomo.jiaolong.user.service.grpc.toModel
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class UserServiceServer(
    private val delegation: UserService,
    coroutineContext: CoroutineContext = EmptyCoroutineContext
) : UserServiceGrpcKt.UserServiceCoroutineImplBase(coroutineContext) {
    override suspend fun register(request: UserServiceOuterClass.RegisterRequest) =
        delegation.register(request.user.toDomain())
            .toModel()
            .let {
                registerResponse {
                    id = it
                }
            }

    override suspend fun find(request: UserServiceOuterClass.FindRequest): UserServiceOuterClass.FindResponse =
        delegation.find(request.id.toDomain())
            ?.toModel()
            .let {
                findResponse {
                    if (it != null) user = it
                }
            }
}
