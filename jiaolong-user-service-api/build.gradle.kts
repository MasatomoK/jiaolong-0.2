plugins {
    setup.grpc
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)
    api(lib.bundles.common.proto.implementation)

    runtimeOnly(lib.grpc.netty)

    api(project(Jiaolong.User.domain))

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
