package com.masatomo.jiaolong.core.config.impl

import com.masatomo.jiaolong.core.config.ConfigLoader
import org.koin.dsl.module


val coreConfigModule = module {
    single<ConfigLoader> { ConfigLoaderImpl() }
}
