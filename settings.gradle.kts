rootProject.name = "jiaolong-0.2"

include("jiaolong-lib-numeric")
include("jiaolong-core-ksp")
include("jiaolong-core-domain")
include("jiaolong-core-application")
include("jiaolong-core-config")
include("jiaolong-core-repository")
include("jiaolong-core-rdb")
include("jiaolong-core-grpc-server")
include("jiaolong-core-system")
include("jiaolong-admin-service")
include("jiaolong-admin-server")
include("jiaolong-sample-domain")
include("jiaolong-sample-repository")
include("jiaolong-sample-service")
include("jiaolong-sample-service-grpc")
include("jiaolong-master-domain")
include("jiaolong-master-repository")
include("jiaolong-master-service")
include("jiaolong-master-service-grpc")
include("jiaolong-first-server")


dependencyResolutionManagement {
    versionCatalogs {
        create("lib") {
            // Core
            val kotlinVersion = version("kotlin", "1.9.+")
            library("kotlin-reflect", "org.jetbrains.kotlin", "kotlin-reflect").versionRef(kotlinVersion)
            library("kotlin-testJunit", "org.jetbrains.kotlin", "kotlin-test-junit").versionRef(kotlinVersion)

            val kotlinxVersion = version("kotlinx", "1.5.+")
            library("kotlinx-serialization-json", "org.jetbrains.kotlinx", "kotlinx-serialization-json").versionRef(
                kotlinxVersion
            )

            val kspVersion = version("ksp", "1.9.+")
            library("ksp-api", "com.google.devtools.ksp", "symbol-processing-api").versionRef(kspVersion)

            val javaxVersion = version("javax", "1.3.+")
            library("javax-annotation-api", "javax.annotation", "javax.annotation-api").versionRef(javaxVersion)

            // Code Generation
            val poetVersion = version("kotlinpoet", "1.15.+")
            library("kotlinpoet-ksp", "com.squareup", "kotlinpoet-ksp").versionRef(poetVersion)

            // Logging
            val slf4jVersion = version("slf4j", "2.0.+")
            library("slf4j-api", "org.slf4j", "slf4j-api").versionRef(slf4jVersion)

            val logbackVersion = version("logback", "1.4.+")
            library("logback-classic", "ch.qos.logback", "logback-classic").versionRef(logbackVersion)

            // Config, Property
            val config4kVersion = version("config4k", "0.6.+")
            library("config4k", "io.github.config4k", "config4k").versionRef(config4kVersion)

            // DI
            val koinVersion = version("koin", "3.5.+")
            library("koin-core", "io.insert-koin", "koin-core").versionRef(koinVersion)
            library("koin-logger-slf4j", "io.insert-koin", "koin-logger-slf4j").versionRef(koinVersion)
            library("koin-ktor", "io.insert-koin", "koin-ktor").versionRef(koinVersion)
            library("koin-test", "io.insert-koin", "koin-test").versionRef(koinVersion)
            library("koin-test-junit5", "io.insert-koin", "koin-test-junit5").versionRef(koinVersion)

            // Database
            val hikariVersion = version("hikari", "5.1.+")
            library("hikari", "com.zaxxer", "HikariCP").versionRef(hikariVersion)

            val exposedVersion = version("exposed", "0.45.+")
            library("exposed-core", "org.jetbrains.exposed", "exposed-core").versionRef(exposedVersion)
            library("exposed-dao", "org.jetbrains.exposed", "exposed-dao").versionRef(exposedVersion)
            library("exposed-jdbc", "org.jetbrains.exposed", "exposed-jdbc").versionRef(exposedVersion)

            val flywayVersion = version("flyway", "10.4.+")
            library("flyway-core", "org.flywaydb", "flyway-core").versionRef(flywayVersion)

            val sqliteVersion = version("sqlite", "3.44.+")
            library("sqlite-jdbc", "org.xerial", "sqlite-jdbc").versionRef(sqliteVersion)
            val mysqlVersion = version("mysql", "8.2.+")
            library("mysql-jdbc", "mysql", "mysql-connector-java").versionRef(mysqlVersion)
            val postgresVersion = version("postgres", "42.7.+")
            library("postgres-jdbc", "org.postgresql", "postgresql").versionRef(postgresVersion)

            // ProtocolBuffer, GRPC
            val protobufVersion = version("protobuf", "3.25.+")
            library("proto-java", "com.google.protobuf", "protobuf-java").versionRef(protobufVersion)
            library("proto-kotlin", "com.google.protobuf", "protobuf-kotlin").versionRef(protobufVersion)
            library("proto-protoc", "com.google.protobuf", "protoc").versionRef(protobufVersion)

            val grpcVersion = version("gRPC", "1.60.+")
            val grpcKotlinVersion = version("gRPCKotlin", "1.4.+")
            library("grpc-api", "io.grpc", "grpc-api").versionRef(grpcVersion)
            library("grpc-protobuf", "io.grpc", "grpc-protobuf").versionRef(grpcVersion)
            library("grpc-gen", "io.grpc", "protoc-gen-grpc-java").versionRef(grpcVersion)
            library("grpc-genKotlin", "io.grpc", "protoc-gen-grpc-kotlin").versionRef(grpcKotlinVersion)
            //    val  = artifact("", "${kotlinVersion}:jdk7@jar")
            library("grpc-stub", "io.grpc", "grpc-stub").versionRef(grpcVersion)
            library("grpc-stubKotlin", "io.grpc", "grpc-kotlin-stub").versionRef(grpcKotlinVersion)
            library("grpc-netty", "io.grpc", "grpc-netty").versionRef(grpcVersion)
            library("grpc-testing", "io.grpc", "grpc-testing").versionRef(grpcVersion)

            // Server
            val ktorVersion = version("ktor", "2.3.+")
            library("ktor-server-core", "io.ktor", "ktor-server-core").versionRef(ktorVersion)
            library("ktor-server-host-common", "io.ktor", "ktor-server-host-common").versionRef(ktorVersion)
            library("ktor-server-netty", "io.ktor", "ktor-server-netty").versionRef(ktorVersion)
            library("ktor-server-cio", "io.ktor", "ktor-server-cio").versionRef(ktorVersion)
            library("ktor-server-content-negotiation", "io.ktor", "ktor-server-content-negotiation").versionRef(
                ktorVersion
            )
            library("ktor-serialization-kotlinx-json", "io.ktor", "ktor-serialization-kotlinx-json").versionRef(
                ktorVersion
            )
            library("ktor-server-auth", "io.ktor", "ktor-server-auth").versionRef(ktorVersion)
            library("ktor-server-cors", "io.ktor", "ktor-server-cors").versionRef(ktorVersion)
            library("ktor-server-call-logging", "io.ktor", "ktor-server-call-logging").versionRef(ktorVersion)
            library("ktor-server-call-logging-jvm", "io.ktor", "ktor-server-call-logging-jvm").versionRef(ktorVersion)
            library("ktor-server-openapi", "io.ktor", "ktor-server-openapi").versionRef(ktorVersion)
            library("ktor-server-swagger", "io.ktor", "ktor-server-swagger").versionRef(ktorVersion)

            val graphqlKotlinVersion = version("graphql", "7.0.+")
            library("graphql-kotlin-ktor-server", "com.expediagroup", "graphql-kotlin-ktor-server").versionRef(
                graphqlKotlinVersion
            )

            // Test
            val jUnitVersion = version("jUnit", "5.10.+")
            library("junit5", "org.junit.jupiter", "junit-jupiter").versionRef(jUnitVersion)

            val kotestVersion = version("kotest", "5.8.+")
            library("kotest-runner", "io.kotest", "kotest-runner-junit5").versionRef(kotestVersion)
            library("kotest-assertions", "io.kotest", "kotest-assertions-core").versionRef(kotestVersion)

            // Other


            bundle(
                "common-kotlin-implementation", listOf(
                    "kotlin-reflect",
                    "koin-core",
                    "slf4j-api"
                )
            )
            bundle(
                "common-kotlin-testImplementation", listOf(
                    "kotest-runner",
                    "kotest-assertions",
                    "koin-test",
                    "koin-test-junit5",
                )
            )
            bundle(
                "common-proto-implementation", listOf(
                    "javax-annotation-api",
                    "proto-java",
                    "proto-kotlin",
                    "grpc-stub",
                    "grpc-stubKotlin",
                    "grpc-protobuf",
                )
            )
        }
    }
}


plugins {
    id("org.gradle.toolchains.foojay-resolver") version "0.7.0"
}

toolchainManagement {
    jvm {
        javaRepositories {
            repository("foojay") {
                resolverClass.set(org.gradle.toolchains.foojay.FoojayToolchainResolver::class.java)
            }
        }
    }
}
