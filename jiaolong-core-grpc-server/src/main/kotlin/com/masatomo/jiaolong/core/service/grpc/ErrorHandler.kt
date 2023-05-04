package com.masatomo.jiaolong.core.service.grpc

import com.masatomo.jiaolong.core.server.logging.getLogger
import io.grpc.Status
import io.grpc.StatusException


private val logger = getLogger()

suspend fun <T> globalErrorHandler(main: suspend () -> T): T = try {
    main.invoke()
} catch (e: UnsupportedOperationException) {
    logger.error("Got unknown error.", e)
    throw StatusException(Status.UNIMPLEMENTED)
} catch (e: Exception) {
    logger.error("Got unknown error.", e)
    throw StatusException(Status.INTERNAL)
}
