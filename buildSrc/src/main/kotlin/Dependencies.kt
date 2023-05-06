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

    object Admin : ProjectGroup("") {
        val service = project("jiaolong-admin-service")
        val server = project("jiaolong-admin-server")
    }

    object Sample : ProjectGroup("") {
        val domain = project("jiaolong-sample-domain")
        val repository = project("jiaolong-sample-repository")
        val service = project("jiaolong-sample-service")
        val serviceGrpc = project("jiaolong-sample-service-grpc")
    }

    object Price : ProjectGroup("") {
        val domain = project("jiaolong-price-domain")
    }

    object Customer : ProjectGroup("") {
        val server = project("jiaolong-customer-server")
    }
}


internal const val protocArtifact = "com.google.protobuf:protoc:3.22.+"
internal const val grpcJavaGenArtifact = "io.grpc:protoc-gen-grpc-java:1.54.+"
internal const val grpcKotlinGenArtifact = "io.grpc:protoc-gen-grpc-kotlin:1.3.+:jdk8@jar"
