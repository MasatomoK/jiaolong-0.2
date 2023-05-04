package com.masatomo.jiaolong.core.rdb.impl

import com.masatomo.jiaolong.core.config.ConfigLoader
import com.masatomo.jiaolong.core.domain.values.StringId
import com.masatomo.jiaolong.core.rdb.DataSourceKey
import com.masatomo.jiaolong.core.rdb.DataSourceProvider
import com.masatomo.jiaolong.core.rdb.Migrator
import com.masatomo.jiaolong.core.rdb.MigratorFactoryKey
import com.masatomo.jiaolong.core.rdb.MigratorKey
import com.masatomo.jiaolong.core.rdb.dbMigrationConfigPath
import com.typesafe.config.Config
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.ClassicConfiguration


private fun ConfigLoader.migratorType(key: MigratorKey) =
    loadConfig((dbMigrationConfigPath / key.value))
        .getString("type")
        .let { MigratorFactoryKey(it) }

private fun ConfigLoader.migratorConfig(key: MigratorKey): Config =
    loadConfig((dbMigrationConfigPath / key.value))
        .getConfig("configuration")


internal class MigratorProviderImpl(
    private val configLoader: ConfigLoader,
    private val migratorFactories: List<Migrator.Factory>,
) : Migrator.Provider {

    override fun provide(key: MigratorKey): Migrator {
        val type = configLoader.migratorType(key)
        return migratorFactories
            .first { it.type == type }
            .create(key, configLoader.migratorConfig(key))
    }
}


// Flywayは分離できるため、database-flywayに移動しても良い。
internal class FlywayMigratorFactory(
    private val dataSourceProvider: DataSourceProvider
) : Migrator.Factory {

    override val type = MigratorFactoryKey("flyway")
    override fun create(name: StringId<Migrator>, config: Config): Migrator = object : Migrator {
        private val flyway by lazy {

            val flywayConfig = ClassicConfiguration()

            if (config.hasPath("flyway")) {
                config.getConfig("flyway").entrySet()
                    .associate { "flyway.${it.key}" to it.value.unwrapped() as String }
                    .let { flywayConfig.configure(it) }
            }
            if (config.hasPath("datasource")) {
                dataSourceProvider.provide(DataSourceKey(config.getString("datasource")))
                    .let { flywayConfig.dataSource = it }
            }
            Flyway.configure().configuration(flywayConfig).load()
        }

        override fun initialize() {
            flyway.clean()
            migrate()
        }

        override fun migrate() {
            flyway.migrate()
            flyway.validate()
        }
    }
}
