package com.masatomo.jiaolong.core.config.impl

import com.masatomo.jiaolong.core.config.ConfigLoader
import com.masatomo.jiaolong.core.config.ConfigPath
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory


class ConfigLoaderImpl : ConfigLoader {
    override fun loadConfig(path: ConfigPath): Config =
        ConfigFactory.load().getConfig(path.elements.joinToString("."))
}
