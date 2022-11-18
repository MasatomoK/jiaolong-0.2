import com.github.gradle.node.NodePlugin
import com.github.gradle.node.npm.task.NpmTask
import com.google.protobuf.gradle.GenerateProtoTask
import com.google.protobuf.gradle.ProtobufExtension
import com.google.protobuf.gradle.ProtobufPlugin
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.protobuf
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.task
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.applyStyle() {
    val style = findProperty("projectStyle") as? String ?: ""
    when {
        style.contains("java") -> apply<SetupJava>()
        style.contains("kotlin") -> apply<SetupKotlin>()
        style.contains("grpc") -> apply<SetupGrpc>()
        style.contains("node") -> apply<SetupNode>()
        style.contains("python") -> apply<SetupPython>()
        style.contains("rust") -> apply<SetupRust>()
        style.isBlank() -> apply<SetupBase>()
        else -> logger.warn("Target style not found. [project: ${this.path}, style: $style]")
    }
}

private class SetupBase : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            apply<BasePlugin>()
            apply<IdeaPlugin>()

            tasks.getByName("clean") {
                this as Delete
                delete("$projectDir/out")
            }
        }
    }
}

private class SetupJava : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            apply<SetupBase>()
            apply<JavaBasePlugin>()
            apply<JavaLibraryPlugin>()

            extensions.getByType<JavaPluginExtension>().apply {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
                withJavadocJar()
//                withSourcesJar()
            }

            tasks.withType<JavaCompile>().configureEach {
                options.encoding = "UTF-8"
            }
        }
    }
}

private class SetupKotlin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            apply<SetupJava>()
            apply<KotlinPluginWrapper>()

            dependencies {
                "implementation"(notation(Koin.core))  // TODO: remove this
                "implementation"(notation(SLF4J.simple))

                "testImplementation"(notation(Kotlin.testJunit))
                "testImplementation"(notation(JUnit.junit5))
            }

            tasks.withType<KotlinCompile>().configureEach {
                kotlinOptions {
                    jvmTarget = "17"
                    // freeCompilerArgs += "-Xkey=value"
                }
            }

            tasks.withType<Test> {
                useJUnitPlatform()
                systemProperties = mapOf(
                    "junit.jupiter.execution.parallel.enabled" to "true",
                    "junit.jupiter.execution.parallel.config.strategy" to "dynamic"
                )
                testLogging.showStandardStreams = true
            }

            tasks.getByName("processResources") {
                (this as Copy).exclude("**/.gitkeep")
            }
        }
    }
}

private class SetupGrpc : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            apply<SetupKotlin>()
            apply<ProtobufPlugin>()

            dependencies {
                "api"(notation(Javax.annotation))
                "api"(notation(Protobuf.java))
                "api"(notation(Protobuf.kotlin))
                "api"(notation(GRPC.stub))
                "api"(notation(GRPC.stubKotlin))
                "api"(notation(GRPC.protobuf))

                "testApi"(notation(GRPC.testing))
            }

            extensions.getByType<ProtobufExtension>().apply {
                protoc { artifact = notation(Protobuf.protoc) }

                generatedFilesBaseDir = "$projectDir/src"

                plugins {
                    id("grpc") { artifact = notation(GRPC.gen) }
                    id("grpckt") { artifact = notation(GRPC.genKotlin) }
                }

                generateProtoTasks {
                    all().forEach {
                        it.plugins {
                            id("grpc") { outputSubDir = "java" }
                            id("grpckt") { outputSubDir = "kotlin" }
                        }
                        it.builtins {
                            id("python")
                            id("cpp")
                            id("kotlin")
                        }
                    }
                }
            }

            tasks.withType<GenerateProtoTask> { }

            tasks.withType<KotlinCompile> {
                kotlinOptions {
                    freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
                }
            }

            tasks.getByName("clean") {
                this as Delete
                delete("$projectDir/src/main/cpp")
                delete("$projectDir/src/main/java")
                delete("$projectDir/src/main/kotlin")
                delete("$projectDir/src/main/python")
            }

            tasks.getByName("processResources").dependsOn("generateProto")
        }
    }
}

private class SetupNode : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            apply<SetupBase>()
            apply<NodePlugin>()

            task<NpmTask>("build-web") {
                args.addAll("run", "build")
            }
            tasks.getByName("build").dependsOn("build-web")
        }
    }
}


private class SetupPython : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            apply<SetupBase>()

            tasks.getByName("build") {
                // TODO
            }
        }
    }
}

private class SetupRust : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            apply<SetupBase>()

            tasks.getByName("build") {
                // TODO
            }
        }
    }
}
