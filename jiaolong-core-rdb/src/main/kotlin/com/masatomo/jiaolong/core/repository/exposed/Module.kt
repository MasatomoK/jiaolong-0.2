package com.masatomo.jiaolong.core.repository.exposed

import com.masatomo.jiaolong.core.repository.TransactionBridge
import org.koin.dsl.module


val coreRepositoryTransactionModule = module {
    single<TransactionBridge> { TransactionBridgeImpl(get()) }
}
