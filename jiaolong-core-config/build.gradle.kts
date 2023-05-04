plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)
    api(lib.config4k)

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
