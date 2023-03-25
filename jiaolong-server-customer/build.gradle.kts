plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.ktor.server.core)
    implementation(lib.ktor.server.cio)
    implementation(lib.ktor.server.auth)
    implementation(lib.ktor.server.content.negotiation)
    implementation(lib.ktor.server.cors)
    implementation(lib.ktor.server.call.logging)
    implementation(lib.ktor.server.openapi)
    implementation(lib.ktor.server.swagger)
    implementation(lib.koin.ktor)
    implementation(lib.graphql.kotlin.ktor.server)
    implementation("io.ktor:ktor-server-call-logging-jvm:2.2.4")
}
