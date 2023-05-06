package com.masatomo.jiaolong.sample.service.grpc

import com.masatomo.jiaolong.core.domain.values.IntegralId
import com.masatomo.jiaolong.sample.domain.User
import com.masatomo.jiaolong.sample.grpc.SampleService
import com.masatomo.jiaolong.sample.grpc.findRequest
import com.masatomo.jiaolong.sample.grpc.findResponse
import com.masatomo.jiaolong.sample.grpc.registerRequest
import com.masatomo.jiaolong.sample.grpc.registerResponse


// Register
internal fun registerRequestFrom(user: User) = registerRequest {
    this.user = user.toModel()
}

internal fun SampleService.RegisterRequest.toUser() = user.toDomain()

internal fun registerResponseFrom(id: IntegralId<User>) = registerResponse {
    this.id = id.toModel()
}

internal fun SampleService.RegisterResponse.toId() = id.toDomain<User>()


// Find
internal fun findRequestFrom(id: IntegralId<User>) = findRequest {
    this.id = id.toModel()
}

internal fun SampleService.FindRequest.toId() = id.toDomain<User>()

internal fun findResponseFrom(user: User?) = findResponse {
    user?.let { this.user = it.toModel() }
}

internal fun SampleService.FindResponse.toUser() = user.toDomain()
