package com.masatomo.jiaolong.sample.service.grpc

import com.masatomo.jiaolong.core.domain.values.IntegralId
import com.masatomo.jiaolong.sample.domain.User
import com.masatomo.jiaolong.sample.grpc.*


// Register
internal fun registerRequestFrom(user: User) = registerRequest {
    this.user = user.toModel()
}

internal fun SampleService.RegisterRequest.toUserId() = user.toUserId()

internal fun registerResponseFrom(id: IntegralId<User>) = registerResponse {
    this.id = id.toModel()
}

internal fun SampleService.RegisterResponse.toId() = id.toUserId()


// Find
internal fun findRequestFrom(id: IntegralId<User>) = findRequest {
    this.id = id.toModel()
}

internal fun SampleService.FindRequest.toId() = id.toUserId()

internal fun findResponseFrom(user: User?) = findResponse {
    user?.let { this.user = it.toModel() }
}

internal fun SampleService.FindResponse.toUserId() = user.toUserId()
