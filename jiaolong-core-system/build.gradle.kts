plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)

    implementation(project(Jiaolong.Core.domain))
    implementation(project(Jiaolong.Core.application))

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
