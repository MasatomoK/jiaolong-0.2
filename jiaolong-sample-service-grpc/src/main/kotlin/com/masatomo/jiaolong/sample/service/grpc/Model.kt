package com.masatomo.jiaolong.sample.service.grpc

import com.masatomo.jiaolong.core.domain.values.IntegralId
import com.masatomo.jiaolong.core.domain.values.Name
import com.masatomo.jiaolong.sample.domain.User
import com.masatomo.jiaolong.sample.domain.UserBuilder
import com.masatomo.jiaolong.sample.grpc.SampleModel
import com.masatomo.jiaolong.sample.grpc.user


internal fun User.toModel() = user {
    id = this@toModel.id.value
    name = this@toModel.name.value
    password = this@toModel.password.value
}

internal fun SampleModel.User.toUserId() = UserBuilder()
    .withId(User.Id(id))
    .withName(Name(name))
    .withPassword(User.Password(password))
    .build()

internal inline fun <reified E> IntegralId<E>.toModel() = value

internal fun Long.toUserId() = User.Id(this)
