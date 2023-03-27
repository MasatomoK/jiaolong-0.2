plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)
    api(project(":jiaolong-core-repository"))
    api(project(":jiaolong-user-domain"))

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
