package com.masatomo.jiaolong.user.service.grpc

import com.masatomo.jiaolong.core.domain.values.IntegralId
import com.masatomo.jiaolong.user.domain.Name
import com.masatomo.jiaolong.user.domain.Password
import com.masatomo.jiaolong.user.domain.User
import com.masatomo.jiaolong.user.domain.UserBuilder
import com.masatomo.jiaolong.user.grpc.UserModel
import com.masatomo.jiaolong.user.grpc.user


fun User.toModel() = user {
    id = this@toModel.id.value
    name = this@toModel.name.value
    password = this@toModel.password.value
}

fun UserModel.User.toDomain() = UserBuilder()
    .withId(IntegralId(id))
    .withName(Name(name))
    .withPassword(Password(password))
    .build()

fun <E> IntegralId<E>.toModel() = value

inline fun <reified E> Long.toDomain() = IntegralId<E>(this)
