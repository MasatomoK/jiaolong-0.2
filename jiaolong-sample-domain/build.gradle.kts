plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)

    api(project(Jiaolong.Core.domain))
    ksp(project(Jiaolong.Core.domain))

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
