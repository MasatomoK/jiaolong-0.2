plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)

    implementation(project(Jiaolong.Core.application))

    implementation(project(Jiaolong.Sample.service))
    runtimeOnly(project(Jiaolong.Sample.serviceGrpc))

    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
