plugins {}

allprojects {
    repositories {
        mavenCentral()
    }

    applyStyle()
}

tasks.withType<Wrapper> {
    gradleVersion = "7.5"
}
