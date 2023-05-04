plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)

    implementation(lib.ksp.api)
    implementation(project(Jiaolong.Core.ksp))

    implementation(project(Jiaolong.Core.domain))
    implementation(project(Jiaolong.Core.application))

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
