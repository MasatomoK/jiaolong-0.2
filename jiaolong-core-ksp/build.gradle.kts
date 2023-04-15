plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)
    implementation(lib.ksp.api)

    api(lib.kotlinpoet.ksp)

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
