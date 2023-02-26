plugins {
    `kotlin-dsl`
    `version-catalog`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation(pluginLibs.gradle.kotlin)
    implementation(pluginLibs.gradle.ksp)
    implementation(pluginLibs.gradle.node)
    implementation(pluginLibs.gradle.protobuf)
}

tasks.withType<Wrapper> {
    gradleVersion = "8.0.1"
}
