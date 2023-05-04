package com.masatomo.jiaolong.core.server

import io.ktor.server.engine.ApplicationEngine
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.koinApplication
import kotlin.reflect.jvm.kotlinProperty


class JiaolongApplication internal constructor() {

    private var koinDeclaration: KoinApplication.() -> Unit = {}
    private var ktorDeclaration: Koin.() -> ApplicationEngine = { throw RuntimeException() }

    internal val koinApplication by lazy {
        koinApplication {
            koinDeclaration.invoke(this)
        }
    }

    fun koin(koinDeclaration: KoinApplication.() -> Unit) {
        this.koinDeclaration = koinDeclaration
    }

    fun ktor(ktorDeclaration: Koin.() -> ApplicationEngine) {
        this.ktorDeclaration = ktorDeclaration
    }

    internal fun start() {
        koinApplication.run {
            ktorDeclaration.invoke(koin)
        }.start(wait = true)
    }

    internal fun stop() {
        // TODO
    }
}

fun jiaolongApplication(block: JiaolongApplication.() -> Unit) =
    JiaolongApplication()
        .also(block)
        .let { JiaolongContext(it) }
        .also { globalContext = it }

fun modulesIn(baseClass: String): Array<Module> = Class.forName(baseClass)
    .declaredFields
    .mapNotNull { it.kotlinProperty }
    .map { it.getter }
    .map { it.call() }
    .filterIsInstance<Module>()
    .toTypedArray()
