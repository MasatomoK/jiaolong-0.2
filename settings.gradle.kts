rootProject.name = "jiaolong-0.2"

include("jiaolong-core-ksp")
include("jiaolong-core-domain")
include("jiaolong-core-repository")
include("jiaolong-core-repository-exposed")
include("jiaolong-core-grpc-server")
include("jiaolong-core-grpc-client")
include("jiaolong-core-server")
include("jiaolong-user-domain")
include("jiaolong-user-repository")
include("jiaolong-user-service")
include("jiaolong-user-service-grpc")
include("jiaolong-price-domain")
include("jiaolong-server-customer")


dependencyResolutionManagement {
    versionCatalogs {
        create("lib") {
            // Core
            val kotlinVersion = version("kotlin", "1.8.+")
            library("kotlin-reflect", "org.jetbrains.kotlin", "kotlin-reflect").versionRef(kotlinVersion)
            library("kotlin-testJunit", "org.jetbrains.kotlin", "kotlin-test-junit").versionRef(kotlinVersion)

            val kotlinxVersion = version("kotlinx", "1.5.+")
            library("kotlinx-serialization-json", "org.jetbrains.kotlinx", "kotlinx-serialization-json").versionRef(kotlinxVersion)

            val kspVersion = version("ksp", "1.8.+")
            library("ksp-api", "com.google.devtools.ksp", "symbol-processing-api").versionRef(kspVersion)

            val javaxVersion = version("javax", "1.3.+")
            library("javax-annotation-api", "javax.annotation", "javax.annotation-api").versionRef(javaxVersion)

            // Code Generation
            val poetVersion = version("kotlinpoet", "1.13.+")
            library("kotlinpoet-ksp", "com.squareup", "kotlinpoet-ksp").versionRef(poetVersion)

            // Logging
            val slf4jVersion = version("slf4j", "2.0.+")
            library("slf4j-api", "org.slf4j", "slf4j-api").versionRef(slf4jVersion)

            val logbackVersion = version("logback", "1.4.+")
            library("logback-classic", "ch.qos.logback", "logback-classic").versionRef(logbackVersion)

            // Config, Property
            val config4kVersion = version("config4k", "0.4.+")
            library("config4k", "io.github.config4k", "config4k").versionRef(config4kVersion)

            // DI
            val koinVersion = version("koin", "3.2.+")
            library("koin-core", "io.insert-koin", "koin-core").versionRef(koinVersion)
            library("koin-logger-slf4j", "io.insert-koin", "koin-logger-slf4j").versionRef(koinVersion)
            library("koin-ktor", "io.insert-koin", "koin-ktor").versionRef(koinVersion)
            library("koin-test", "io.insert-koin", "koin-test").versionRef(koinVersion)
            library("koin-test-junit5", "io.insert-koin", "koin-test-junit5").versionRef(koinVersion)

            // Database
            val hikariVersion = version("hikari", "5.0.+")
            library("hikari", "com.zaxxer", "HikariCP").versionRef(hikariVersion)

            val exposedVersion = version("exposed", "0.41.+")
            library("exposed-core", "org.jetbrains.exposed", "exposed-core").versionRef(exposedVersion)
            library("exposed-dao", "org.jetbrains.exposed", "exposed-dao").versionRef(exposedVersion)
            library("exposed-jdbc", "org.jetbrains.exposed", "exposed-jdbc").versionRef(exposedVersion)

            val flywayVersion = version("flyway", "7.11.+")
            library("flyway-core", "org.flywaydb", "flyway-core").versionRef(flywayVersion)

            val sqliteVersion = version("sqlite", "3.36.+")
            library("sqlite-jdbc", "org.xerial", "sqlite-jdbc").versionRef(sqliteVersion)
            val mysqlVersion = version("mysql", "8.0.+")
            library("mysql-jdbc", "mysql", "mysql-connector-java").versionRef(mysqlVersion)

            // ProtocolBuffer, GRPC
            val protobufVersion = version("protobuf", "3.19.+")
            library("proto-java", "com.google.protobuf", "protobuf-java").versionRef(protobufVersion)
            library("proto-kotlin", "com.google.protobuf", "protobuf-kotlin").versionRef(protobufVersion)
            library("proto-protoc", "com.google.protobuf", "protoc").versionRef(protobufVersion)

            val grpcVersion = version("gRPC", "1.41.+")
            val grpcKotlinVersion = version("gRPCKotlin", "1.2.+")
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
            val ktorVersion = version("ktor", "2.2.+")
            library("ktor-server-core", "io.ktor", "ktor-server-core").versionRef(ktorVersion)
            library("ktor-server-host-common", "io.ktor", "ktor-server-host-common").versionRef(ktorVersion)
            library("ktor-server-netty", "io.ktor", "ktor-server-netty").versionRef(ktorVersion)
            library("ktor-server-cio", "io.ktor", "ktor-server-cio").versionRef(ktorVersion)
            library("ktor-server-content-negotiation", "io.ktor", "ktor-server-content-negotiation").versionRef(ktorVersion)
            library("ktor-serialization-kotlinx-json", "io.ktor", "ktor-serialization-kotlinx-json").versionRef(ktorVersion)
            library("ktor-server-auth", "io.ktor", "ktor-server-auth").versionRef(ktorVersion)
            library("ktor-server-cors", "io.ktor", "ktor-server-cors").versionRef(ktorVersion)
            library("ktor-server-call-logging", "io.ktor", "ktor-server-call-logging").versionRef(ktorVersion)
            library("ktor-server-call-logging-jvm", "io.ktor", "ktor-server-call-logging-jvm").versionRef(ktorVersion)
            library("ktor-server-openapi", "io.ktor", "ktor-server-openapi").versionRef(ktorVersion)
            library("ktor-server-swagger", "io.ktor", "ktor-server-swagger").versionRef(ktorVersion)

            val graphqlKotlinVersion = version("graphql", "7.0.+")
            library("graphql-kotlin-ktor-server", "com.expediagroup", "graphql-kotlin-ktor-server").versionRef(graphqlKotlinVersion)

            // Test
            val jUnitVersion = version("jUnit", "5.9.+")
            library("junit5", "org.junit.jupiter", "junit-jupiter").versionRef(jUnitVersion)

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
                    "kotlin-testJunit",
                    "junit5",
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
