plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)
    implementation(project(Jiaolong.Core.domain))

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
