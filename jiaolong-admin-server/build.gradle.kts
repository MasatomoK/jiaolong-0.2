plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)

    implementation(project(Jiaolong.Core.application))
    implementation(project(Jiaolong.Admin.service))
    runtimeOnly(lib.postgres.jdbc)

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
