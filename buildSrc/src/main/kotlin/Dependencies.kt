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
        val ksp = project("jiaolong-core-ksp")
        val domain = project("jiaolong-core-domain")
        val repository = project("jiaolong-core-repository")
    }

    object Price : ProjectGroup("") {
        val domain = project("jiaolong-price-domain")
    }

    object User : ProjectGroup("") {
        val domain = project("jiaolong-user-domain")
    }
}

object Protobuf : ArtifactGroup("com.google.protobuf", "3.19.+") {
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
