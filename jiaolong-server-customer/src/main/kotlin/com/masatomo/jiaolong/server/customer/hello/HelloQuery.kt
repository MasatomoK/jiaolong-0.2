package com.masatomo.jiaolong.server.customer.hello

import com.expediagroup.graphql.server.operations.Mutation
import com.expediagroup.graphql.server.operations.Query

object HelloQuery : Query {
    fun hello() = "Hello World!"
    fun hello2(req: String) = "Hello World! $req"
}


data class Payload(val x: String)

object HelloMutation : Mutation {

    fun hello(hoge: String): Payload {

        return Payload(hoge)
    }
}
