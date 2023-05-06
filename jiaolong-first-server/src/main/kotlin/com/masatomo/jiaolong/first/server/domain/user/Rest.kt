package com.masatomo.jiaolong.first.server.domain.user

import com.masatomo.jiaolong.core.domain.values.IntegralId
import com.masatomo.jiaolong.sample.domain.Name
import com.masatomo.jiaolong.sample.domain.Password
import com.masatomo.jiaolong.sample.domain.User
import com.masatomo.jiaolong.sample.domain.UserBuilder
import com.masatomo.jiaolong.sample.service.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondNullable
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject


fun Route.configureUser() {

    val service by inject<UserService>()

    post<CreateUserRequest>("/user/create") {
        service.register(it.toDomain())
            .let { id -> call.respond(CreateUserResponse(id.value)) }
    }

    get("/user/get") {
        call.parameters["id"]
            ?.toLong()
            ?.let { service.find(IntegralId(it)) }
            ?.let { GetUserResponse(it) }
            .let { call.respondNullable(HttpStatusCode.OK, it) }
    }
}

@Serializable
data class CreateUserRequest(
    val name: String,
    val password: String
) {
    fun toDomain(): User = UserBuilder()
        .withId(IntegralId.unassigned())
        .withName(Name(name))
        .withPassword(Password(name))
        .build()
}

@Serializable
data class CreateUserResponse(
    val id: Long,
)

@Serializable
data class GetUserResponse(
    val id: Long,
    val name: String,
    val password: String
) {
    internal constructor(user: User) : this(
        user.id.value,
        user.name.value,
        user.password.value,
    )
}