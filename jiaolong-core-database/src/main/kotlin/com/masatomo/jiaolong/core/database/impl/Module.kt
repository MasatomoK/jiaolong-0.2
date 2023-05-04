package com.masatomo.jiaolong.core.database.impl

import com.masatomo.jiaolong.core.database.DataSourceFactory
import com.masatomo.jiaolong.core.database.DataSourceProvider
import com.masatomo.jiaolong.core.database.DatabaseProvider
import com.masatomo.jiaolong.core.database.Migrator
import org.koin.dsl.module

val coreDatabaseModule = module {
    single<DatabaseProvider> { DatabaseProviderImpl(get(), get()) }
}

val coreMigratorModule = module {
    single<Migrator.Provider> { MigratorProviderImpl(get(), getAll()) }
    single<Migrator.Factory> { FlywayMigratorFactory(get()) }
}

val coreDataSourceModule = module {
    single<DataSourceProvider> { DataSourceProviderImpl(get(), getAll()) }
    single<DataSourceFactory> { HikariDataSourceFactory() }
}
