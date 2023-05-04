package com.masatomo.jiaolong.core.server

import org.koin.core.Koin


internal var globalContext: JiaolongContext? = null
fun globalContext() = globalContext

data class JiaolongContext(
    val application: JiaolongApplication
) {

    val koin: Koin
        get() = application.koinApplication.koin

    fun start() {
        application.start()
    }

    fun stop() {
        application.stop()
    }
}
