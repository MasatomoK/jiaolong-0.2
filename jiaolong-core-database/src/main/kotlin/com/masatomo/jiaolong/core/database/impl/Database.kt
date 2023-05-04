package com.masatomo.jiaolong.core.database.impl

import com.masatomo.jiaolong.core.config.ConfigLoader
import com.masatomo.jiaolong.core.database.DataSourceKey
import com.masatomo.jiaolong.core.database.DataSourceProvider
import com.masatomo.jiaolong.core.database.DatabaseKey
import com.masatomo.jiaolong.core.database.DatabaseProvider
import com.masatomo.jiaolong.core.database.dbDefinitionConfigPath
import org.jetbrains.exposed.sql.Database


private fun ConfigLoader.databaseSourceKey(key: DatabaseKey) =
    loadConfig((dbDefinitionConfigPath / key.value))
        .getString("datasource")
        .let { DataSourceKey(it) }

fun ConfigLoader.scopeId(key: DatabaseKey) =
    loadConfig((dbDefinitionConfigPath / key.value))
        .getString("scopeId")
        .let { DataSourceKey(it) }

internal class DatabaseProviderImpl(
    private val configLoader: ConfigLoader,
    private val dataSourceProvider: DataSourceProvider,
) : DatabaseProvider {
    override fun create(key: DatabaseKey): Database =
        configLoader.databaseSourceKey(key)
            .let { dataSourceProvider.provide(it) }
            .let { Database.connect(it) }
}
