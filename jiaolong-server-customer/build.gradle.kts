plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.ktor.server.core)
    implementation(lib.ktor.server.cio)
    implementation(lib.ktor.server.auth)
    implementation(lib.ktor.server.content.negotiation)
    implementation(lib.ktor.serialization.kotlinx.json)
    implementation(lib.ktor.server.cors)
    implementation(lib.ktor.server.call.logging)
    implementation(lib.ktor.server.call.logging.jvm)
    implementation(lib.ktor.server.openapi)
    implementation(lib.ktor.server.swagger)
    implementation(lib.koin.ktor)
    implementation(lib.graphql.kotlin.ktor.server)
    implementation(lib.logback.classic)

    implementation(lib.kotlinx.serialization.json)

    implementation(project(":jiaolong-user-domain"))
    implementation(project(":jiaolong-user-repository"))
    implementation(project(":jiaolong-user-service"))
}
