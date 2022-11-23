package com.masatomo.jiaolong.price.domain

import com.masatomo.jiaolong.core.common.BigDecimalValue
import java.math.BigDecimal


@JvmInline
@BigDecimalValue.Validations.Maximum("")
value class Price(override val value: BigDecimal) : BigDecimalValue<Price> {
}

@JvmInline
@BigDecimalValue.Validations.Maximum("")
value class Volume(override val value: BigDecimal) : BigDecimalValue<Volume> {
}

@JvmInline
@BigDecimalValue.Validations.Maximum("")
value class DateTime(override val value: BigDecimal) : BigDecimalValue<Price> {
}
