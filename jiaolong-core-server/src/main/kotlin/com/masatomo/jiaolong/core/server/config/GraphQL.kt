package com.masatomo.jiaolong.core.server.config

import com.expediagroup.graphql.server.ktor.DefaultKtorGraphQLContextFactory
import com.expediagroup.graphql.server.ktor.GraphQL
import com.expediagroup.graphql.server.ktor.GraphQLConfiguration
import com.expediagroup.graphql.server.ktor.graphQLGetRoute
import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import com.expediagroup.graphql.server.ktor.graphQLSDLRoute
import com.expediagroup.graphql.server.ktor.graphiQLRoute
import com.masatomo.jiaolong.core.server.definition.ApplicationDefinition
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.routing


fun Application.graphQL(configure: GraphQLConfiguration.() -> Unit) {

    install(GraphQL) {
        configure()
    }

    routing {
        graphQLGetRoute()
        graphQLPostRoute()
        graphiQLRoute()
        graphQLSDLRoute()
    }
}

fun GraphQLConfiguration.configureDefault(definition: ApplicationDefinition) {
    schema {
        packages = definition.packages
        queries = definition.queries
        mutations = definition.mutations
    }

    engine {
        // TODO
    }

    server {
        contextFactory = DefaultKtorGraphQLContextFactory()
    }
}
