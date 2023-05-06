package com.masatomo.jiaolong.sample.domain

import com.masatomo.jiaolong.core.domain.DomainEntity
import com.masatomo.jiaolong.core.domain.EntityBuilder
import com.masatomo.jiaolong.core.domain.values.IntegralId


@EntityBuilder
data class User(
    override val id: IntegralId<User>,
    var name: Name,
    var password: Password,
) : DomainEntity<User, IntegralId<User>> {
    override fun assigned(id: IntegralId<User>) = copy(id = id)
    override fun toString() = reflectionToString()
}

fun main() {
    val user = UserBuilder()
        .withId(IntegralId(12))
        .withName(Name("ABC"))
        .withPassword(Password("aa"))
        .also { println(it) }
        .build()
    println(user.toString())
}
