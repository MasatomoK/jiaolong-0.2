package com.masatomo.jiaolong.core.config

import com.typesafe.config.Config


data class ConfigPath(val elements: Array<String>) {
    companion object {
        fun of(vararg elements: String) = ConfigPath(arrayOf(*elements))
    }

    operator fun plus(other: ConfigPath) = ConfigPath(this.elements + other.elements)
    operator fun plus(other: String) = ConfigPath(this.elements + other)
    operator fun div(other: ConfigPath) = ConfigPath(this.elements + other.elements)
    operator fun div(other: String) = ConfigPath(this.elements + other)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return elements.contentEquals((other as ConfigPath).elements)
    }

    override fun hashCode(): Int = elements.contentHashCode()
}

interface ConfigLoader {
    fun loadConfig(path: ConfigPath): Config
    fun loadConfig(vararg rootKey: String) = loadConfig(ConfigPath(arrayOf(*rootKey)))
    // fun <T : Any> load(baseName: String, rootKey: String): T
}
