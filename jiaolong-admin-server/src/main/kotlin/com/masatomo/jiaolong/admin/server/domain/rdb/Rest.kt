package com.masatomo.jiaolong.admin.server.domain.rdb

import com.masatomo.jiaolong.admin.service.AdminService
import com.masatomo.jiaolong.core.rdb.MigratorKey
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respondNullable
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject


fun Route.configureRdb() {

    val service by inject<AdminService>()

    route("/rdb") {
        post<InitializeRdbRequest>("/initialize") {
            service.initialize(it.toDomain())
            call.respondNullable<Any?>(HttpStatusCode.OK, null)
        }

        post<MigrateRdbRequest>("/migrate") {
            service.migrate(it.toDomain())
            call.respondNullable<Any?>(HttpStatusCode.OK, null)
        }
    }
}


@Serializable
data class InitializeRdbRequest(
    val key: String,
) {
    fun toDomain(): MigratorKey = MigratorKey(key)
}

@Serializable
data class MigrateRdbRequest(
    val key: String,
) {
    fun toDomain(): MigratorKey = MigratorKey(key)
}
