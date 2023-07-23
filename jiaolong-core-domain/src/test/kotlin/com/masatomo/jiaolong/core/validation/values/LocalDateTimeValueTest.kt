package com.masatomo.jiaolong.core.validation.values

import com.masatomo.jiaolong.core.domain.values.LocalDateTimeValue
import com.masatomo.jiaolong.core.validation.InvalidValidationException
import com.masatomo.jiaolong.core.validation.validate
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.time.DayOfWeek
import java.time.LocalDateTime

class LocalDateTimeTimeValueTest : FunSpec({
    context("AfterLocalDateTime") {
        test("2000-01-02 is after 2000-01-01") {
            shouldNotThrow<InvalidValidationException> {
                AfterLocalDateTimeValueTestee(LocalDateTime.of(2000, 1, 2, 0, 0, 0))
            }
        }

        test("2000-01-01 is not more than 2000-01-01") {
            shouldThrow<InvalidValidationException> {
                AfterLocalDateTimeValueTestee(LocalDateTime.of(2000, 1, 1, 0, 0, 0))
            }.apply {
                kClass shouldBeEqual AfterLocalDateTimeValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual NotAfterLocalDateTime(
                    AfterLocalDateTimeValueTestee::class,
                    LocalDateTime.of(2000, 1, 1, 0, 0, 0),
                    LocalDateTime.of(2000, 1, 1, 0, 0, 0),
                )
            }
        }
    }

    context("BeforeLocalDateTime") {
        test("1999-12-31 is before 2000-01-01") {
            shouldNotThrow<InvalidValidationException> {
                BeforeLocalDateTimeValueTestee(LocalDateTime.of(1999, 12, 31, 0, 0, 0))
            }
        }

        test("2000-01-01 is not more than 2000-01-01") {
            shouldThrow<InvalidValidationException> {
                BeforeLocalDateTimeValueTestee(LocalDateTime.of(2000, 1, 1, 0, 0, 0))
            }.apply {
                kClass shouldBeEqual BeforeLocalDateTimeValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual NotBeforeLocalDateTime(
                    BeforeLocalDateTimeValueTestee::class,
                    LocalDateTime.of(2000, 1, 1, 0, 0, 0),
                    LocalDateTime.of(2000, 1, 1, 0, 0, 0),
                )
            }
        }
    }

    context("DayOfWeekLocalDateTime") {
        test("2000-01-01 is Saturday") {
            shouldNotThrow<InvalidValidationException> {
                DayOfWeekLocalDateTimeValueTestee(LocalDateTime.of(2000, 1, 1, 0, 0, 0))
            }
        }

        test("2000-01-03 is Monday") {
            shouldThrow<InvalidValidationException> {
                DayOfWeekLocalDateTimeValueTestee(LocalDateTime.of(2000, 1, 3, 0, 0, 0))
            }.apply {
                kClass shouldBeEqual DayOfWeekLocalDateTimeValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual OuterDayOfWeekLocalDateTime(
                    DayOfWeekLocalDateTimeValueTestee::class,
                    LocalDateTime.of(2000, 1, 3, 0, 0, 0),
                    setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY),
                )
            }
        }
    }
})

private class AfterLocalDateTimeValueTestee(override val value: LocalDateTime) : LocalDateTimeValue {
    init {
        validate {
            after(LocalDateTime.of(2000, 1, 1, 0, 0, 0))
        }
    }
}

private class BeforeLocalDateTimeValueTestee(override val value: LocalDateTime) : LocalDateTimeValue {
    init {
        validate {
            before(LocalDateTime.of(2000, 1, 1, 0, 0, 0))
        }
    }
}

private class DayOfWeekLocalDateTimeValueTestee(override val value: LocalDateTime) : LocalDateTimeValue {
    init {
        validate {
            dayOfWeeksIn(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
        }
    }
}
