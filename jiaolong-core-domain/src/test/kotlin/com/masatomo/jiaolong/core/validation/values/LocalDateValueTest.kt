package com.masatomo.jiaolong.core.validation.values

import com.masatomo.jiaolong.core.domain.values.LocalDateValue
import com.masatomo.jiaolong.core.validation.InvalidValidationException
import com.masatomo.jiaolong.core.validation.validate
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.time.DayOfWeek
import java.time.LocalDate

class LocalDateValueTest : FunSpec({
    context("AfterLocalDate") {
        test("2000-01-02 is after 2000-01-01") {
            shouldNotThrow<InvalidValidationException> {
                AfterLocalDateValueTestee(LocalDate.of(2000, 1, 2))
            }
        }

        test("2000-01-01 is not more than 2000-01-01") {
            shouldThrow<InvalidValidationException> {
                AfterLocalDateValueTestee(LocalDate.of(2000, 1, 1))
            }.apply {
                kClass shouldBeEqual AfterLocalDateValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual NotAfterLocalDate(
                    AfterLocalDateValueTestee::class,
                    LocalDate.of(2000, 1, 1),
                    LocalDate.of(2000, 1, 1),
                )
            }
        }
    }

    context("BeforeLocalDate") {
        test("1999-12-31 is before 2000-01-01") {
            shouldNotThrow<InvalidValidationException> {
                BeforeLocalDateValueTestee(LocalDate.of(1999, 12, 31))
            }
        }

        test("2000-01-01 is not more than 2000-01-01") {
            shouldThrow<InvalidValidationException> {
                BeforeLocalDateValueTestee(LocalDate.of(2000, 1, 1))
            }.apply {
                kClass shouldBeEqual BeforeLocalDateValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual NotBeforeLocalDate(
                    BeforeLocalDateValueTestee::class,
                    LocalDate.of(2000, 1, 1),
                    LocalDate.of(2000, 1, 1),
                )
            }
        }
    }

    context("DayOfWeekLocalDate") {
        test("2000-01-01 is Saturday") {
            shouldNotThrow<InvalidValidationException> {
                DayOfWeekLocalDateValueTestee(LocalDate.of(2000, 1, 1))
            }
        }

        test("2000-01-03 is Monday") {
            shouldThrow<InvalidValidationException> {
                DayOfWeekLocalDateValueTestee(LocalDate.of(2000, 1, 3))
            }.apply {
                kClass shouldBeEqual DayOfWeekLocalDateValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual OuterDayOfWeek(
                    DayOfWeekLocalDateValueTestee::class,
                    LocalDate.of(2000, 1, 3),
                    setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY),
                )
            }
        }
    }
})

private class AfterLocalDateValueTestee(override val value: LocalDate) : LocalDateValue {
    init {
        validate {
            after(LocalDate.of(2000, 1, 1))
        }
    }
}

private class BeforeLocalDateValueTestee(override val value: LocalDate) : LocalDateValue {
    init {
        validate {
            before(LocalDate.of(2000, 1, 1))
        }
    }
}

private class DayOfWeekLocalDateValueTestee(override val value: LocalDate) : LocalDateValue {
    init {
        validate {
            dayOfWeeksIn(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
        }
    }
}
