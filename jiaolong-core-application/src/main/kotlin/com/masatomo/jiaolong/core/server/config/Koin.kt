package com.masatomo.jiaolong.core.server.config

import com.masatomo.jiaolong.core.server.definition.ApplicationDefinition
import org.koin.core.KoinApplication

fun KoinApplication.registerModules(definition: ApplicationDefinition) {
    modules(definition.modules)
}
