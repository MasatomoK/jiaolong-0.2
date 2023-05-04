plugins {
    setup.grpc
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)
    api(lib.bundles.common.proto.implementation)

    runtimeOnly(project(Jiaolong.Core.config))

    implementation(project(Jiaolong.Core.application))
    implementation(project(Jiaolong.Core.grpcServer))

    runtimeOnly(project(Jiaolong.Core.rdb))
    runtimeOnly(lib.sqlite.jdbc)

    implementation(project(Jiaolong.User.repository))
    implementation(project(Jiaolong.User.service))

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
