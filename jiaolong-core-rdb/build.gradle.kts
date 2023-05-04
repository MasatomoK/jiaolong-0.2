plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)

    implementation(project(Jiaolong.Core.ksp))

    implementation(project(Jiaolong.Core.domain))
    implementation(project(Jiaolong.Core.config))

    implementation(project(Jiaolong.Core.repository))
    api(lib.exposed.core)
    implementation(lib.exposed.jdbc)
    implementation(lib.hikari)
    implementation(lib.flyway.core)

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
