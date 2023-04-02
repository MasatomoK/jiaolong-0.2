package com.masatomo.jiaolong.core.service.grpc

import io.grpc.BindableService
import io.grpc.Server
import io.grpc.ServerBuilder
import io.ktor.server.application.ApplicationStopPreparing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.ApplicationEngineEnvironment
import io.ktor.server.engine.ApplicationEngineFactory
import io.ktor.server.engine.BaseApplicationEngine
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext


object GRpc : ApplicationEngineFactory<GRpcApplicationEngine, GRpcApplicationEngine.Configuration> {
    override fun create(
        environment: ApplicationEngineEnvironment,
        configure: GRpcApplicationEngine.Configuration.() -> Unit
    ) = GRpcApplicationEngine(environment, configure)
}

class GRpcApplicationEngine(
    environment: ApplicationEngineEnvironment,
    configure: Configuration.() -> Unit = {}
) : BaseApplicationEngine(environment) {

    class Configuration : BaseApplicationEngine.Configuration() {
        internal val serviceFactory: MutableList<(CoroutineContext) -> BindableService> = mutableListOf()
        fun service(body: (CoroutineContext) -> BindableService) = serviceFactory.add(body)
    }

    private val configuration = Configuration().apply(configure)
    private val servers: List<Server> by lazy {
        environment.connectors.map {
            ServerBuilder
                .forPort(it.port)
                .apply {
                    configuration.serviceFactory
                        .map { it(environment.parentCoroutineContext) }
                        .forEach(::addService)
                }
                .build()
        }
    }

    override fun start(wait: Boolean): ApplicationEngine = this.also {
        servers.forEach(Server::start)
        Runtime.getRuntime().addShutdownHook(
            Thread {
                stop()
            }
        )
        if (wait) servers.forEach(Server::awaitTermination)
    }

    override fun stop(gracePeriodMillis: Long, timeoutMillis: Long) {
        environment.monitor.raise(ApplicationStopPreparing, environment)
        println("*** shutting down gRPC server since JVM is shutting down")
        servers.forEach {
            it.awaitTermination(gracePeriodMillis, TimeUnit.MILLISECONDS)
            it.shutdownNow()
        }
        println("*** server shut down")
    }
}
