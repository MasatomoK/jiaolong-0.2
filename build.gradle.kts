plugins {
    `kotlin-dsl`
}

allprojects {
    repositories {
        mavenCentral()
    }
}

tasks.withType(Delete::class).getByName("clean") {
    delete("$projectDir/docs")
    delete("$projectDir/buildSrc/build")
}

tasks.withType<Wrapper> {
    gradleVersion = "8.0.2"
}
