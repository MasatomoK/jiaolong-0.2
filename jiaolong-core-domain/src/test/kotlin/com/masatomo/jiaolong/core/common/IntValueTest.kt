package com.masatomo.jiaolong.core.common

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


@IntValue.Validations.Minimum(0)
private class MinInt(
    override val value: Int
) : IntValue<MinInt>

class IntValueTest {
    @Test
    fun test_normal() {
        assertEquals(
            MinInt(0).isValid(), emptyList()
        )
    }

    @Test
    fun test_invalid_minimum() {
        assertEquals(
            MinInt(-1).isValid(),
            listOf(IntValue.Validations.Minimum.LessMinimumInt(MinInt::class, -1, 0, true))
        )
    }
}
