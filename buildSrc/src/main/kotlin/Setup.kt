import com.github.gradle.node.NodePlugin
import com.github.gradle.node.npm.task.NpmTask
import com.google.devtools.ksp.gradle.KspGradleSubplugin
import com.google.protobuf.gradle.GenerateProtoTask
import com.google.protobuf.gradle.ProtobufExtension
import com.google.protobuf.gradle.ProtobufPlugin
import com.google.protobuf.gradle.id
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
import org.gradle.jvm.tasks.Jar
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JvmVendorSpec
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.task
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


open class SetupBase : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            apply<BasePlugin>()
            apply<IdeaPlugin>()

            tasks.withType(Delete::class).getByName("clean") {
                delete("$projectDir/out")
            }
        }
    }
}

class SetupJava : SetupBase() {
    override fun apply(target: Project) {
        target.run {
            super.apply(target)
            apply<JavaBasePlugin>()
            apply<JavaLibraryPlugin>()

            extensions.getByType<JavaPluginExtension>().apply {
                withJavadocJar()
                withSourcesJar()
                toolchain {
                    languageVersion.set(JavaLanguageVersion.of(17))
                    vendor.set(JvmVendorSpec.AMAZON)
                }
            }

            tasks.withType<JavaCompile>().configureEach {
                options.encoding = "UTF-8"
            }

            tasks.withType<Jar> {
                exclude(".gitkeep")
            }
        }
    }
}

class SetupKotlin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            val runKsp = findProperty("runKsp") == "true"

            apply<SetupJava>()
            apply<KotlinPluginWrapper>()
            if (runKsp) apply<KspGradleSubplugin>()

            extensions.getByType<KotlinProjectExtension>().apply {
                if (runKsp) {
                    sourceSets.getByName("main") {
                        kotlin.srcDir("build/generated/ksp/main/kotlin")
                    }
                    sourceSets.getByName("test") {
                        kotlin.srcDir("build/generated/ksp/test/kotlin")
                    }
                }
                jvmToolchain {
                    languageVersion.set(JavaLanguageVersion.of(17))
                    vendor.set(JvmVendorSpec.AMAZON)
                }
            }

            tasks.withType<KotlinCompile>().configureEach {
                kotlinOptions {
                    apiVersion = "1.8"
                    languageVersion = "1.8"
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

            tasks.getByName<Copy>("processResources") {
                exclude("**/.gitkeep")
            }
        }
    }
}

class SetupGrpc : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            apply<SetupKotlin>()
            apply<ProtobufPlugin>()

            extensions.getByType<ProtobufExtension>().apply {
                protoc { artifact = notation(Protobuf.protoc) }

                // generatedFilesBaseDir = "$projectDir/src"

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

            tasks.getByName<Delete>("clean") {
                delete("$projectDir/src/main/cpp")
                delete("$projectDir/src/main/java")
                delete("$projectDir/src/main/kotlin")
                delete("$projectDir/src/main/python")
            }

            tasks.getByName("processResources").dependsOn("generateProto")
        }
    }
}

class SetupNode : Plugin<Project> {
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


class SetupPython : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            apply<SetupBase>()

            tasks.getByName("build") {
                // TODO
            }
        }
    }
}

class SetupRust : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            apply<SetupBase>()

            tasks.getByName("build") {
                // TODO
            }
        }
    }
}
