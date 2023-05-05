package com.masatomo.jiaolong.core.rdb

import com.masatomo.jiaolong.core.config.ConfigPath
import com.masatomo.jiaolong.core.domain.values.StringId
import com.typesafe.config.Config
import org.jetbrains.exposed.sql.Database
import javax.sql.DataSource


val databaseRootConfigPath = ConfigPath.of("jiaolong", "core", "database")
val dbDefinitionConfigPath = databaseRootConfigPath / "definitions"
val dbMigrationConfigPath = databaseRootConfigPath / "migrator"
val dbDataSourceConfigPath = databaseRootConfigPath / "datasource"


typealias DatabaseKey = StringId<Database>

interface DatabaseProvider {
    fun create(key: DatabaseKey): Database
}


typealias DataSourceKey = StringId<DataSource>
typealias DataSourceFactoryKey = StringId<DataSourceFactory>

interface DataSourceProvider {
    fun provide(key: DataSourceKey): DataSource
}

interface DataSourceFactory {
    val type: DataSourceFactoryKey
    fun create(name: DataSourceKey, config: Config): DataSource
}


typealias MigratorKey = StringId<Migrator>
typealias MigratorFactoryKey = StringId<Migrator.Factory>

interface Migrator {
    interface Provider {
        fun provide(key: MigratorKey): Migrator
    }

    interface Factory {
        val type: MigratorFactoryKey
        fun create(name: StringId<Migrator>, config: Config): Migrator
    }

    fun initialize()
    fun migrate()
}
