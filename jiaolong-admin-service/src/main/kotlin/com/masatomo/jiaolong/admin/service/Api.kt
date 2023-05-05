package com.masatomo.jiaolong.admin.service

import com.masatomo.jiaolong.core.rdb.MigratorKey


interface AdminService {
    fun initialize(key: MigratorKey)
    fun migrate(key: MigratorKey)
}
