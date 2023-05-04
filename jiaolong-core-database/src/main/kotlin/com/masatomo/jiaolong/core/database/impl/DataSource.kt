package com.masatomo.jiaolong.core.database.impl

import com.masatomo.jiaolong.core.config.ConfigLoader
import com.masatomo.jiaolong.core.database.DataSourceFactory
import com.masatomo.jiaolong.core.database.DataSourceFactoryKey
import com.masatomo.jiaolong.core.database.DataSourceKey
import com.masatomo.jiaolong.core.database.DataSourceProvider
import com.masatomo.jiaolong.core.database.dbDataSourceConfigPath
import com.masatomo.jiaolong.core.domain.values.StringId
import com.typesafe.config.Config
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.io.Closeable
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.sql.DataSource


private fun ConfigLoader.dataSourceType(key: DataSourceKey) =
    loadConfig((dbDataSourceConfigPath / key.value))
        .getString("type")
        .let { DataSourceFactoryKey(it) }

private fun ConfigLoader.dataSourceConfig(key: DataSourceKey): Config =
    loadConfig((dbDataSourceConfigPath / key.value))
        .getConfig("configuration")


internal class DataSourceProviderImpl(
    private val configLoader: ConfigLoader,
    private val dataSourceFactories: List<DataSourceFactory>,
) : DataSourceProvider {

    private val cache = ConcurrentHashMap<DataSourceKey, DataSource>()
    override fun provide(key: DataSourceKey) = cache.computeIfAbsent(key) {
        val type = configLoader.dataSourceType(key)
        dataSourceFactories
            .first { it.type == type }
            .create(key, configLoader.dataSourceConfig(key))
    }
}


internal class HikariDataSourceFactory : DataSourceFactory, Closeable {

    override val type = StringId<DataSourceFactory>("hikari")
    private val cache = ConcurrentHashMap<DataSourceKey, HikariDataSource>() // TODO: remove this

    override fun create(name: DataSourceKey, config: Config): DataSource = cache.computeIfAbsent(name) {
        Properties()
            .also { props ->
                config.entrySet().forEach { props[it.key] = it.value.unwrapped() }
            }
            .let { HikariConfig(it) }
            .let { HikariDataSource(it) }
    }

    override fun close() {
        cache.forEachValue(1) { if (!it.isClosed) it.close() }
    }
}
