package com.masatomo.jiaolong.price.domain

import com.masatomo.jiaolong.core.domain.values.BigDecimalValidations
import com.masatomo.jiaolong.core.domain.values.BigDecimalValue
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
