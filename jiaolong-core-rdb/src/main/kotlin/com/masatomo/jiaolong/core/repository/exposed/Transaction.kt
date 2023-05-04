package com.masatomo.jiaolong.core.repository.exposed

import com.masatomo.jiaolong.core.rdb.DatabaseKey
import com.masatomo.jiaolong.core.rdb.DatabaseProvider
import com.masatomo.jiaolong.core.repository.TransactionBridge
import com.masatomo.jiaolong.core.repository.TransactionScope
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlin.coroutines.CoroutineContext


class TransactionBridgeImpl(
    private val databaseProvider: DatabaseProvider
) : TransactionBridge {

    private fun resolveDatabase(scope: TransactionScope): Database =
        databaseProvider.create(DatabaseKey(scope.id.value))

    override fun <S : TransactionScope, T> transaction(scope: S, statement: S.() -> T): T =
        resolveDatabase(scope).let {
            org.jetbrains.exposed.sql.transactions.transaction(it) {
                statement.invoke(scope)
            }
        }

    override suspend fun <S : TransactionScope, T> transaction(scope: S, context: CoroutineContext?, statement: suspend S.() -> T): T =
        resolveDatabase(scope).let {
            newSuspendedTransaction(context, it, null) {
                statement.invoke(scope)
            }
        }
}
