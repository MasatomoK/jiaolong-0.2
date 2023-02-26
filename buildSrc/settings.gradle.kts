dependencyResolutionManagement {
    versionCatalogs {
        create("pluginLibs") {
            val kotlinVersion = version("kotlin", "1.8.10")
            library("gradle-kotlin", "org.jetbrains.kotlin", "kotlin-gradle-plugin").versionRef(kotlinVersion)

            val kspVersion = version("ksp", "1.8.10-+")
            library("gradle-ksp", "com.google.devtools.ksp", "com.google.devtools.ksp.gradle.plugin").versionRef(kspVersion)

            val nodePluginVersion = version("node", "3.5.+")
            library("gradle-node", "com.github.node-gradle", "gradle-node-plugin").versionRef(nodePluginVersion)

            val protoVersion = version("proto", "0.9.+")
            library("gradle-protobuf", "com.google.protobuf", "protobuf-gradle-plugin").versionRef(protoVersion)
        }
    }
}
