dependencyResolutionManagement {
    versionCatalogs {
        create("pluginLibs") {
            val kotlinVersion = version("kotlin", "1.9.20")
            library("gradle-kotlin", "org.jetbrains.kotlin", "kotlin-gradle-plugin").versionRef(kotlinVersion)
            library(
                "gradle-kotlinx-serialization",
                "org.jetbrains.kotlin.plugin.serialization",
                "org.jetbrains.kotlin.plugin.serialization.gradle.plugin"
            ).versionRef(kotlinVersion)

            val kspVersion = version("ksp", "1.9.22-+")
            library("gradle-ksp", "com.google.devtools.ksp", "com.google.devtools.ksp.gradle.plugin").versionRef(
                kspVersion
            )

            val nodePluginVersion = version("node", "7.0.+")
            library("gradle-node", "com.github.node-gradle", "gradle-node-plugin").versionRef(nodePluginVersion)

            val protoVersion = version("proto", "0.9.+")
            library("gradle-protobuf", "com.google.protobuf", "protobuf-gradle-plugin").versionRef(protoVersion)
        }
    }
}
