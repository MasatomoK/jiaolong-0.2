plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)

    api(project(Jiaolong.Core.repository))
    ksp(project(Jiaolong.Core.repository))
    implementation(project(Jiaolong.Core.rdb))
    ksp(project(Jiaolong.Core.rdb))

    api(project(Jiaolong.Sample.domain))

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
