plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)

    implementation(project(Jiaolong.Core.application))
    implementation(project(Jiaolong.Admin.service))

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
