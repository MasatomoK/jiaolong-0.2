plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)

    api(lib.ktor.server.core)
    api(lib.ktor.server.cio)
    api(lib.ktor.server.auth)
    api(lib.ktor.server.content.negotiation)
    api(lib.ktor.serialization.kotlinx.json)
    api(lib.ktor.server.cors)
    api(lib.ktor.server.call.logging)
    api(lib.ktor.server.call.logging.jvm)
    api(lib.ktor.server.openapi)
    api(lib.ktor.server.swagger)
    api(lib.koin.ktor)
    api(lib.graphql.kotlin.ktor.server)
    runtimeOnly(lib.logback.classic)
    runtimeOnly(lib.kotlinx.serialization.json)

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
