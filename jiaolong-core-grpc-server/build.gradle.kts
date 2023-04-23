plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)

    runtimeOnly(lib.logback.classic)

    api(project(Jiaolong.Core.domain))
    api(lib.ktor.server.core)
    api(lib.ktor.server.host.common)
    implementation(lib.grpc.api)
    runtimeOnly(lib.grpc.netty)

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
