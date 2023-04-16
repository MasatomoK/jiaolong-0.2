package com.masatomo.jiaolong.price.domain

import com.masatomo.jiaolong.core.domain.DomainEntity
import com.masatomo.jiaolong.core.domain.values.IntegralId


data class Tick(
    override val id: IntegralId<Tick>,
    val time: DateTime,
) : DomainEntity<Tick, IntegralId<Tick>> {
    override fun assigned(id: IntegralId<Tick>): Tick {
        TODO("Not yet implemented")
    }
}
