package com.masatomo.jiaolong.core.config

import com.masatomo.jiaolong.core.config.impl.ConfigLoaderImpl
import org.koin.dsl.module


val coreConfigModule = module {
    single<ConfigLoader> { ConfigLoaderImpl() }
}
