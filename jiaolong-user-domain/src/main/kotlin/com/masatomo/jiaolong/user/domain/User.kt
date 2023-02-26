package com.masatomo.jiaolong.user.domain

import com.masatomo.jiaolong.core.common.IntegralId
import com.masatomo.jiaolong.core.domain.DomainEntity
import com.masatomo.jiaolong.core.domain.EntityBuilder


@EntityBuilder
class User(
    override val id: IntegralId<User>,
    val name: Name
) : DomainEntity<User> {
    override fun toString() = reflectionToString()
}

fun main() {
    val user = UserBuilder()
        .withId(IntegralId(12))
        .withName(Name("ABC"))
        .also { println(it) }
        .build()
    println(user.toString())
}
