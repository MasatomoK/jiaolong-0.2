plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)

    implementation(project(Jiaolong.Core.grpcServer))

    api(project(Jiaolong.User.serviceApi))
    implementation(project(Jiaolong.User.repository))

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
