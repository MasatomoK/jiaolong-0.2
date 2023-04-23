plugins {
    setup.grpc
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)
    api(lib.bundles.common.proto.implementation)

    runtimeOnly(lib.grpc.netty)
    implementation(project(Jiaolong.Core.grpcServer))

    implementation(project(Jiaolong.User.repository))
    implementation(project(Jiaolong.User.service))

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
