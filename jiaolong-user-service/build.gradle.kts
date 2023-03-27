plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)
    api(project(":jiaolong-user-domain"))
    implementation(project(":jiaolong-user-repository"))

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
