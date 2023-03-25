package com.masatomo.jiaolong.server.customer.application

import com.expediagroup.graphql.server.ktor.DefaultKtorGraphQLContextFactory
import com.expediagroup.graphql.server.ktor.GraphQL
import com.expediagroup.graphql.server.ktor.GraphQLConfiguration
import com.expediagroup.graphql.server.ktor.graphQLGetRoute
import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import com.expediagroup.graphql.server.ktor.graphQLSDLRoute
import com.expediagroup.graphql.server.ktor.graphiQLRoute
import com.masatomo.jiaolong.server.customer.hello.HelloMutation
import com.masatomo.jiaolong.server.customer.hello.HelloQuery
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.routing


internal fun Application.graphQL(configure: GraphQLConfiguration.() -> Unit) {

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

internal fun GraphQLConfiguration.configure() {
    schema {
        packages = listOf("com.masatomo.jiaolong.server.customer")
        queries = listOf(
            HelloQuery
        )
        mutations = listOf(
            HelloMutation
        )
    }

    engine {
        // TODO
    }

    server {
        contextFactory = DefaultKtorGraphQLContextFactory()
    }
}
