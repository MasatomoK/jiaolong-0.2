package com.masatomo.jiaolong.core.rdb.impl

import com.masatomo.jiaolong.core.config.ConfigLoader
import com.masatomo.jiaolong.core.rdb.DataSourceKey
import com.masatomo.jiaolong.core.rdb.DataSourceProvider
import com.masatomo.jiaolong.core.rdb.DatabaseKey
import com.masatomo.jiaolong.core.rdb.DatabaseProvider
import com.masatomo.jiaolong.core.rdb.dbDefinitionConfigPath
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
