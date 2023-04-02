plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)

    implementation(project(Jiaolong.Core.server))

    implementation(project(Jiaolong.User.serviceApi))

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
