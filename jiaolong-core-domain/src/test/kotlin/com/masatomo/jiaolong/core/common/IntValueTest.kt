package com.masatomo.jiaolong.core.common

import com.masatomo.jiaolong.core.domain.values.IntValidations
import com.masatomo.jiaolong.core.domain.values.IntValue
import com.masatomo.jiaolong.core.domain.values.LessMinimumInt
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


@IntValidations.Minimum(0)
private class MinInt(
    override val value: Int
) : IntValue

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
            listOf(LessMinimumInt(MinInt::class, -1, 0, true))
        )
    }
}
