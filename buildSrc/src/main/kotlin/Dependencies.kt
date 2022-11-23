import org.gradle.api.Project


open class ArtifactGroup(val name: String, val version: String? = null) {
    internal fun artifact(name: String, version: String? = null) = Artifact(this@ArtifactGroup.name, name, version ?: this@ArtifactGroup.version)
    inner class Artifact(val group: String, val name: String, val version: String?)
}

@Suppress("UNCHECKED_CAST")  // なぜかキャストが必要
open class ProjectGroup(private vararg val paths: String = arrayOf("")) {
    internal fun project(name: String) = Project(*(paths as Array<String>).plus(name))
    inner class Project(private vararg val paths: String) {
        val path get() = paths.joinToString(":")
    }
}

fun Project.notation(artifact: ArtifactGroup.Artifact): String = "${artifact.group}:${artifact.name}:${version(artifact)}"
fun Project.project(project: ProjectGroup.Project) = project(project.path)

private fun Project.version(artifact: ArtifactGroup.Artifact): String =
    findProperty("${artifact.group}:${artifact.name}.version") as String?
        ?: findProperty("${artifact.group}.version") as String?
        ?: artifact.version
        ?: throw RuntimeException("Cannot find artifact version. []")


object Jiaolong {
    object Core : ProjectGroup("", ) {
        val domain = project("jiaolong-core-domain")
    }

    object Price : ProjectGroup("") {
        val domain = project("jiaolong-price-domain")
    }
}

object Javax : ArtifactGroup("javax.annotation", "1.3.+") {
    val annotation = artifact("javax.annotation-api")
}

object Kotlin : ArtifactGroup("org.jetbrains.kotlin", "1.7.+") {
    val gradlePlugin = artifact("kotlin-gradle-plugin")
    val reflect = artifact("kotlin-reflect")
    val testJunit = artifact("kotlin-test-junit")
}

object Config4K : ArtifactGroup("io.github.config4k", "0.4.+") {
    val main = artifact("config4k")
}

object SLF4J : ArtifactGroup("org.slf4j", "1.7.+") {
    val simple = artifact("slf4j-simple")
}

object Koin : ArtifactGroup("io.insert-koin", "3.2.+") {
    val core = artifact("koin-core")
    val slf4j = artifact("koin-logger-slf4j")
    val ktor = artifact("koin-ktor")
    val test = artifact("koin-test")
    val junit5 = artifact("koin-test-junit5")
}

object HikariCP : ArtifactGroup("com.zaxxer", "5.0.+") {
    val connection = artifact("HikariCP")
}

object Exposed : ArtifactGroup("org.jetbrains.exposed", "0.37.+") {
    val core = artifact("exposed-core")
    val dao = artifact("exposed-dao")
    val jdbc = artifact("exposed-jdbc")
}

object Flyway : ArtifactGroup("org.flywaydb", "7.11.+") {
    val core = artifact("flyway-core")
}

object Ktor : ArtifactGroup("io.ktor", "2.0.+") {
    val core = artifact("ktor-server-core")
    val serverHostCommon = artifact("ktor-server-host-common")
    val netty = artifact("ktor-server-netty")
    val cio = artifact("ktor-server-cio")
    val auth = artifact("ktor-server-auth")
}

object Sqlite3 : ArtifactGroup("org.xerial", "3.36.+") {
    val jdbc = artifact("sqlite-jdbc")
}

object MySQL : ArtifactGroup("mysql", "8.0.+") {
    val jdbc = artifact("mysql-connector-java")
}

object Protobuf : ArtifactGroup("com.google.protobuf", "3.19.+") {
    private const val gradleVersion = "0.8.+"

    val gradlePlugin = artifact("protobuf-gradle-plugin", gradleVersion)
    val java = artifact("protobuf-java")
    val kotlin = artifact("protobuf-kotlin")
    val protoc = artifact("protoc")
}

object GRPC : ArtifactGroup("io.grpc", "1.41.+") {
    private const val kotlinVersion = "1.2.+"

    val api = artifact("grpc-api")
    val protobuf = artifact("grpc-protobuf")
    val gen = artifact("protoc-gen-grpc-java")
    val genKotlin = artifact("protoc-gen-grpc-kotlin", "${kotlinVersion}:jdk7@jar")
    val stub = artifact("grpc-stub")
    val stubKotlin = artifact("grpc-kotlin-stub", kotlinVersion)
    val netty = artifact("grpc-netty")
    val testing = artifact("grpc-testing")
}

object JUnit : ArtifactGroup("org.junit.jupiter", "5.9.+") {
    val junit5 = artifact("junit-jupiter")
}

object Node : ArtifactGroup("com.github.node-gradle", "3.1.+") {
    val gradlePlugin = artifact("gradle-node-plugin")
}
