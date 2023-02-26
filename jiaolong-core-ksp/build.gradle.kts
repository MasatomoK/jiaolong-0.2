plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)
    implementation(lib.ksp.api)

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
