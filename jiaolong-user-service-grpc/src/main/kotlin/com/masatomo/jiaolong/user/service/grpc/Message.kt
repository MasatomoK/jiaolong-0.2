package com.masatomo.jiaolong.user.service.grpc

import com.masatomo.jiaolong.core.domain.values.IntegralId
import com.masatomo.jiaolong.user.domain.User
import com.masatomo.jiaolong.user.grpc.UserServiceOuterClass
import com.masatomo.jiaolong.user.grpc.findRequest
import com.masatomo.jiaolong.user.grpc.findResponse
import com.masatomo.jiaolong.user.grpc.registerRequest
import com.masatomo.jiaolong.user.grpc.registerResponse


// Register
internal fun registerRequestFrom(user: User) = registerRequest {
    this.user = user.toModel()
}

internal fun UserServiceOuterClass.RegisterRequest.toUser() = user.toDomain()

internal fun registerResponseFrom(id: IntegralId<User>) = registerResponse {
    this.id = id.toModel()
}

internal fun UserServiceOuterClass.RegisterResponse.toId() = id.toDomain<User>()


// Find
internal fun findRequestFrom(id: IntegralId<User>) = findRequest {
    this.id = id.toModel()
}

internal fun UserServiceOuterClass.FindRequest.toId() = id.toDomain<User>()

internal fun findResponseFrom(user: User?) = findResponse {
    user?.let { this.user = it.toModel() }
}

internal fun UserServiceOuterClass.FindResponse.toUser() = user.toDomain()
