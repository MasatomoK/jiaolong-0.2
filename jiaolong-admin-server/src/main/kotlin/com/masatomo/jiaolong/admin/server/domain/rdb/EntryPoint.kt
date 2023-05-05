package com.masatomo.jiaolong.admin.server.domain.rdb

import com.masatomo.jiaolong.admin.service.adminServiceModule
import com.masatomo.jiaolong.core.server.definition.entryPoint
import com.masatomo.jiaolong.core.server.modulesIn
import io.ktor.server.routing.Route


internal val rdbEntryPoint = entryPoint {
    module {
        includes(adminServiceModule)
        includes(*modulesIn("com.masatomo.jiaolong.core.config.ModuleKt"))
        includes(*modulesIn("com.masatomo.jiaolong.core.rdb.ModuleKt"))
    }
//    graphQL {
//        `package`("com.masatomo.jiaolong.admin.server.domain.migrate")
//        query(XxxQuery)
//        mutation(XxxMutation)
//    }
    route(Route::configureRdb)
}
