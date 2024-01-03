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
    object Lib : ProjectGroup("") {
        val numeric = project("jiaolong-lib-numeric")
    }

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

    object Master : ProjectGroup("") {
        val domain = project("jiaolong-master-domain")
        val repository = project("jiaolong-master-repository")
        val service = project("jiaolong-master-service")
        val serviceGrpc = project("jiaolong-master-service-grpc")
    }

    object Sample : ProjectGroup("") {
        val domain = project("jiaolong-sample-domain")
        val repository = project("jiaolong-sample-repository")
        val service = project("jiaolong-sample-service")
        val serviceGrpc = project("jiaolong-sample-service-grpc")
    }

    object First : ProjectGroup("") {
        val server = project("jiaolong-customer-server")
    }
}


internal const val protocArtifact = "com.google.protobuf:protoc:3.25.+"
internal const val grpcJavaGenArtifact = "io.grpc:protoc-gen-grpc-java:1.60.+"
internal const val grpcKotlinGenArtifact = "io.grpc:protoc-gen-grpc-kotlin:1.4.+:jdk8@jar"
