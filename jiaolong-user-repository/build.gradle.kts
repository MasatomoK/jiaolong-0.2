plugins {
    setup.kotlin
}

dependencies {
    implementation(lib.bundles.common.kotlin.implementation)

    api(project(Jiaolong.Core.repository))
    ksp(project(Jiaolong.Core.repository))
    implementation(project(Jiaolong.Core.repositoryExposed))
    ksp(project(Jiaolong.Core.repositoryExposed))
    api(project(Jiaolong.User.domain))


    testImplementation(lib.bundles.common.kotlin.testImplementation)
}
