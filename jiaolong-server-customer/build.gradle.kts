plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)

    implementation(lib.kotlinx.serialization.json)

    implementation(project(":jiaolong-core-server"))

    implementation(project(":jiaolong-user-domain"))
    implementation(project(":jiaolong-user-repository"))
    implementation(project(":jiaolong-user-service"))

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
