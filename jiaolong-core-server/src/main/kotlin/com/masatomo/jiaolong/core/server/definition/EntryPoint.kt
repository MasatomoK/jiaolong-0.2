package com.masatomo.jiaolong.core.server.definition

import com.expediagroup.graphql.server.operations.Mutation
import com.expediagroup.graphql.server.operations.Query
import io.ktor.server.routing.Route
import org.koin.core.module.Module
import org.koin.dsl.module
import kotlin.reflect.jvm.kotlinProperty


class ApplicationDefinition(
    private vararg val entryPoints: EntryPoint,
) {
    internal val modules: List<Module>
        get() = entryPoints.map { it.module }

    internal val packages: List<String>
        get() = entryPoints.flatMap { it.graphQLDef.packages }
    internal val queries: List<Query>
        get() = entryPoints.flatMap { it.graphQLDef.queries }
    internal val mutations: List<Mutation>
        get() = entryPoints.flatMap { it.graphQLDef.mutations }

    internal val route: List<Route.() -> Unit>
        get() = entryPoints.map { it.route }
}


fun entryPoint(configure: EntryPoint.() -> Unit): EntryPoint {
    return EntryPoint().also(configure)
}


class EntryPoint(
    internal val module: Module = module { },
    internal val graphQLDef: GraphQLDefinition = GraphQLDefinition(),
    internal var route: Route.() -> Unit = {}
) {
    fun module(config: Module.() -> Unit) {
        module.config()
    }

    fun modulesIn(baseClass: String): Array<Module> = Class.forName(baseClass)
        .declaredFields
        .mapNotNull { it.kotlinProperty }
        .map { it.getter }
        .map { it.call() }
        .filterIsInstance<Module>()
        .toTypedArray()

    fun graphQL(config: GraphQLDefinition.() -> Unit) {
        graphQLDef.config()
    }

    fun route(config: Route.() -> Unit) {
        route = config
    }
}

class GraphQLDefinition(
    internal val packages: MutableList<String> = mutableListOf(),
    internal val queries: MutableList<Query> = mutableListOf(),
    internal val mutations: MutableList<Mutation> = mutableListOf(),
) {
    fun `package`(`package`: String) {
        packages.add(`package`)
    }

    fun query(query: Query) {
        queries.add(query)
    }

    fun mutation(mutation: Mutation) {
        mutations.add(mutation)
    }
}
