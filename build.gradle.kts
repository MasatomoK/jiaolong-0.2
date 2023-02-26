plugins {
    `kotlin-dsl`
}

allprojects {
    repositories {
        mavenCentral()
    }
}

tasks.withType<Wrapper> {
    gradleVersion = "8.0.1"
}
