plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)

    api(project(Jiaolong.Core.domain))

    api(project(Jiaolong.Core.rdb))
    runtimeOnly(lib.sqlite.jdbc)

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
