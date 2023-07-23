package com.masatomo.jiaolong.first.server.domain.user

import com.masatomo.jiaolong.core.domain.values.Name
import com.masatomo.jiaolong.sample.domain.User
import com.masatomo.jiaolong.sample.domain.UserBuilder
import com.masatomo.jiaolong.sample.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject


fun Route.configureUser() {

    val service by inject<UserService>()

    post<CreateUserRequest>("/user/create") {
        it.toDomain().let { service.register(it) }

            .let { id -> call.respond(CreateUserResponse(id.value)) }
    }

    get("/user/get") {
        call.parameters["id"]
            ?.toLong()
            ?.let { service.find(User.Id(it)) }
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
        .withId(User.Id.DEFAULT)
        .withName(Name(name))
        .withPassword(User.Password(password))
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
