plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)
    implementation(lib.ksp.api)

    implementation(project(Jiaolong.Core.ksp))
    implementation(project(Jiaolong.Core.domain))

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
