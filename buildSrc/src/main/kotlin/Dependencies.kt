import org.gradle.api.Project


@Suppress("UNCHECKED_CAST")  // なぜかキャストが必要
open class ProjectGroup(private vararg val paths: String = arrayOf("")) {
    internal fun project(name: String) = Project(*(paths as Array<String>).plus(name))
    inner class Project(private vararg val paths: String) {
        val path get() = paths.joinToString(":")
    }
}
fun Project.project(project: ProjectGroup.Project): Project = project(project.path)

object Jiaolong {
    object Core : ProjectGroup("") {
        val ksp = project("jiaolong-core-ksp")
        val domain = project("jiaolong-core-domain")
        val application = project("jiaolong-core-application")
        val config = project("jiaolong-core-config")
        val repository = project("jiaolong-core-repository")
        val rdb = project("jiaolong-core-rdb")
        val grpcServer = project("jiaolong-core-grpc-server")
        val system = project("jiaolong-core-system")
    }

    object User : ProjectGroup("") {
        val domain = project("jiaolong-user-domain")
        val repository = project("jiaolong-user-repository")
        val service = project("jiaolong-user-service")
        val serviceGrpc = project("jiaolong-user-service-grpc")
    }

    object Price : ProjectGroup("") {
        val domain = project("jiaolong-price-domain")
    }

    object Server : ProjectGroup("") {
        val customer = project("jiaolong-server-customer")
    }
}


internal const val protocArtifact = "com.google.protobuf:protoc:3.22.+"
internal const val grpcJavaGenArtifact = "io.grpc:protoc-gen-grpc-java:1.54.+"
internal const val grpcKotlinGenArtifact = "io.grpc:protoc-gen-grpc-kotlin:1.3.+:jdk8@jar"
