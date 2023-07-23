package com.masatomo.jiaolong.sample.domain

import com.masatomo.jiaolong.core.domain.DomainEntity
import com.masatomo.jiaolong.core.domain.EntityBuilder
import com.masatomo.jiaolong.core.domain.values.IntegralId
import com.masatomo.jiaolong.core.domain.values.Name
import com.masatomo.jiaolong.core.domain.values.StringValue
import com.masatomo.jiaolong.core.validation.validate
import com.masatomo.jiaolong.core.validation.values.NameConstraint
import com.masatomo.jiaolong.core.validation.values.maximumLength


@EntityBuilder
@NameConstraint.MaxLength(20)
data class User(
    override val id: Id = Id.DEFAULT,
    var name: Name<User>,
    var password: Password,
) : DomainEntity<User, User.Id> {
    init {
        validate {
            // TODO
        }
    }

    override fun assigned(id: Id) = copy(id = id)
    override fun toString() = reflectionToString()

    @JvmInline
    value class Id(override val value: Long) : IntegralId<User> {
        companion object {
            val DEFAULT = Id(-1L)
        }
    }

    @JvmInline
    value class Password(override val value: String) : StringValue {
        init {
            validate {
                maximumLength(20)
            }
        }
    }
}
