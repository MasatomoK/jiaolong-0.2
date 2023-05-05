package com.masatomo.jiaolong.admin.service.impl

import com.masatomo.jiaolong.admin.service.AdminService
import com.masatomo.jiaolong.core.rdb.Migrator
import com.masatomo.jiaolong.core.rdb.MigratorKey

internal class AdminServiceImpl(
    private val migratorProvider: Migrator.Provider
) : AdminService {
    private fun provideMigratorOf(key: MigratorKey) = migratorProvider.provide(key)
    override fun initialize(key: MigratorKey) = provideMigratorOf(key).initialize()
    override fun migrate(key: MigratorKey) = provideMigratorOf(key).migrate()
}
