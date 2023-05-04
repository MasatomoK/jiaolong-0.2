package com.masatomo.jiaolong.core.rdb

import com.masatomo.jiaolong.core.rdb.impl.DataSourceProviderImpl
import com.masatomo.jiaolong.core.rdb.impl.DatabaseProviderImpl
import com.masatomo.jiaolong.core.rdb.impl.FlywayMigratorFactory
import com.masatomo.jiaolong.core.rdb.impl.HikariDataSourceFactory
import com.masatomo.jiaolong.core.rdb.impl.MigratorProviderImpl
import org.koin.dsl.module

val coreRdbModule = module {
    single<DatabaseProvider> { DatabaseProviderImpl(get(), get()) }
}

val coreRdbMigratorModule = module {
    single<Migrator.Provider> { MigratorProviderImpl(get(), getAll()) }
    single<Migrator.Factory> { FlywayMigratorFactory(get()) }
}

val coreRdbDataSourceModule = module {
    single<DataSourceProvider> { DataSourceProviderImpl(get(), getAll()) }
    single<DataSourceFactory> { HikariDataSourceFactory() }
}
