plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)

    implementation(lib.ksp.api)
    implementation(project(Jiaolong.Core.ksp))

    implementation(project(Jiaolong.Core.domain))

    api(project(Jiaolong.Core.repository))
    api(lib.exposed.core)
    implementation(lib.exposed.jdbc)
    implementation(project(Jiaolong.Core.database))

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
