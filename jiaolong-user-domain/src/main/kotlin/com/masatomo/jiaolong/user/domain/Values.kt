package com.masatomo.jiaolong.user.domain

import com.masatomo.jiaolong.core.common.StringValidations
import com.masatomo.jiaolong.core.common.StringValue


@JvmInline
@StringValidations.MaximumLength(20)
value class Name(override val value: String) : StringValue

@JvmInline
@StringValidations.MaximumLength(20)
value class Password(override val value: String) : StringValue
