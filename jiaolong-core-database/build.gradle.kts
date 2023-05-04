plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)
    api(project(Jiaolong.Core.config))
    api(project(Jiaolong.Core.domain))
    implementation(lib.exposed.jdbc)
    implementation(lib.hikari)
    implementation(lib.flyway.core)

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
