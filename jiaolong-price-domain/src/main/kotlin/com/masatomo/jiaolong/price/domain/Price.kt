package com.masatomo.jiaolong.price.domain

import com.masatomo.jiaolong.core.common.BigDecimalValidations
import com.masatomo.jiaolong.core.common.BigDecimalValue
import java.math.BigDecimal


@JvmInline
@BigDecimalValidations.Maximum("")
value class Price(override val value: BigDecimal) : BigDecimalValue

@JvmInline
@BigDecimalValidations.Maximum("")
value class Volume(override val value: BigDecimal) : BigDecimalValue

@JvmInline
@BigDecimalValidations.Maximum("")
value class DateTime(override val value: BigDecimal) : BigDecimalValue
