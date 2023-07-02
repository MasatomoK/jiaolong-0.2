package com.masatomo.jiaolong.core.domain


/**
 * This exception must be thrown at the case of caller can **not remove** the cause.
 */
open class ApplicationException(val code: String) : Exception()

/**
 * This exception must be thrown at the case of caller can **remove** the cause.
 */
open class ApplicationRuntimeException(val code: String) : RuntimeException()
