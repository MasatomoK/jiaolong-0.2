package com.masatomo.jiaolong.core.repository

import com.masatomo.jiaolong.core.domain.DomainEntity
import com.masatomo.jiaolong.core.domain.values.Id
import com.masatomo.jiaolong.core.domain.values.StringId
import com.masatomo.jiaolong.core.server.globalContext
import org.koin.mp.KoinPlatformTools
import kotlin.coroutines.CoroutineContext


interface Repository<E : DomainEntity<E, I>, I : Id<E>> {
    fun register(entity: E): I

    fun findAll(): Iterable<E>
    fun findById(id: I): E?

    fun update(entity: E)

    fun delete(id: I)
}

data class Paging(
    val perPage: Int,
    val page: Int,
)


interface TransactionScope {
    val id: StringId<TransactionScope>
}

fun <S : TransactionScope, T> transaction(scope: S, statement: S.() -> T): T =
    globalContext()!!.koin.get<TransactionBridge>()
        .transaction(scope, statement)

suspend fun <S : TransactionScope, T> suspendTransaction(scope: S, context: CoroutineContext? = null, statement: suspend S.() -> T): T =
    globalContext()!!.koin.get<TransactionBridge>()
        .transaction(scope, context, statement)

interface TransactionBridge {
    fun <S : TransactionScope, T> transaction(scope: S, statement: S.() -> T): T
    suspend fun <S : TransactionScope, T> transaction(scope: S, context: CoroutineContext?, statement: suspend S.() -> T): T
}
