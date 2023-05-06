plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)

    api(project(Jiaolong.Sample.domain))
    implementation(project(Jiaolong.Sample.repository))

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
