plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)

    implementation(project(Jiaolong.Core.server))

    implementation(project(Jiaolong.User.service))
    implementation(project(Jiaolong.User.serviceGrpc))

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
