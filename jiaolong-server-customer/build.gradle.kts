plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)

    implementation(project(Jiaolong.Core.application))

    implementation(project(Jiaolong.User.service))
    runtimeOnly(project(Jiaolong.User.serviceGrpc))

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
