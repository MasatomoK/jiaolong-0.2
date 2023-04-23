plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)

    api(project(Jiaolong.User.domain))
    implementation(project(Jiaolong.User.repository))

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
