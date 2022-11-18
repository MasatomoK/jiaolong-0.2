plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

kotlinDslPluginOptions {
    jvmTarget.set("17")
}

dependencies {
    // 本当はArtifactsから取りたいがビルドのタイミングが良くないので、ハードコードする。
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.+")
    implementation("com.github.node-gradle:gradle-node-plugin:3.5.+")
    implementation("com.google.protobuf:protobuf-gradle-plugin:0.9.+")
}
