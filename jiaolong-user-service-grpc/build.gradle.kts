plugins {
    setup.grpc
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)
    api(lib.bundles.common.proto.implementation)

    runtimeOnly(project(Jiaolong.Core.database))
    runtimeOnly(lib.sqlite.jdbc)

    implementation(project(Jiaolong.Core.application))
    implementation(project(Jiaolong.Core.grpcServer))
    runtimeOnly(lib.grpc.netty)

    runtimeOnly(project(Jiaolong.Core.config))

    implementation(project(Jiaolong.User.repository))
    runtimeOnly(project(Jiaolong.Core.repositoryExposed))
    implementation(project(Jiaolong.User.service))

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
