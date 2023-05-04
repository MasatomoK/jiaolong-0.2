plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)

    api(lib.ksp.api)
    api(lib.kotlinpoet.ksp)

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
