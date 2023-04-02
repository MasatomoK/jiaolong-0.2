plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)

    api(project(Jiaolong.Core.repository))
    api(project(Jiaolong.User.domain))

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
