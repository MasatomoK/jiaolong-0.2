plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)

    implementation(project(Jiaolong.Core.application))
    implementation(lib.ktor.server.core)
    implementation(lib.ktor.server.host.common)

    implementation(lib.grpc.api)
    runtimeOnly(lib.grpc.netty)

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
