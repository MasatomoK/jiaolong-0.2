package com.masatomo.jiaolong.core.domain.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.symbol.Nullability
import com.google.devtools.ksp.validate
import com.masatomo.jiaolong.core.domain.EntityBuilder
import org.koin.ext.getFullName
import java.io.OutputStream
import java.util.*


class BuilderProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor = BuilderProcessor(
        environment.codeGenerator,
        environment.logger
    )
}

private class BuilderProcessor(
    val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.info("Processor is running")

        val (targets, nonTarget) = resolver
            .getSymbolsWithAnnotation(EntityBuilder::class.getFullName())
            .partition { it.validate() }

        targets.asSequence()
            .filter { it is KSClassDeclaration && it.validate() }
            .forEach {
                logger.info("$it")
                it.accept(BuilderVisitor(), Unit)
            }
        return nonTarget
    }

    inner class BuilderVisitor : KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            logger.info("visit class: $classDeclaration")
            classDeclaration.primaryConstructor!!.accept(this, data)
        }

        override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
            logger.info("visit function: $function.")

            val parent = function.parentDeclaration as KSClassDeclaration
            val packageName = parent.containingFile!!.packageName.asString()
            val parameters = function.parameters
            val className = "${parent.simpleName.asString()}Builder"

            codeGenerator.createNewFile(Dependencies(true, function.containingFile!!), packageName, className).use { file ->
                file.appendText(0, "package $packageName", 2)

                //  file.appendText(0, "// import XXX", 2)

                file.appendText(0, "class $className (")
                parameters.forEach {
                    file.appendText(1, "private var ${it.name!!.asString()}: ${it.typeName}? = null,")
                }
                file.appendText(0, ") {")

                parameters.forEach {
                    val name = it.name!!.asString()
                    val funName = "with" + name.replaceFirstChar { it.titlecase(Locale.getDefault()) }
                    file.appendText(1, "fun $funName($name: ${it.typeName}): $className {")
                    file.appendText(2, "this.$name = $name")
                    file.appendText(2, "return this")
                    file.appendText(1, "}")
                    file.newline()
                }

                file.appendText(1, "fun build(): ${parent.qualifiedName!!.asString()} {")
                file.appendText(2, "return ${parent.qualifiedName!!.asString()}(")
                file.appendText(3, parameters.joinToString(", ") { "${it.name!!.asString()}!!" })
                file.appendText(2, ").also{ it.isValid() }")
                file.appendText(1, "}")
                file.newline()

                file.appendText(1, "override fun toString(): String {")
                file.appendText(2, "return \"${parent.simpleName.asString()}Builder(\" +")
                parameters
                    .map { it.name!!.asString() }
                    .map { "$it=\$$it" }
                    .joinToString(", ")
                    .let { file.appendText(4, "\"$it\" +") }
                file.appendText(4, "\")\"")
                file.appendText(1, "}")

                file.appendText(0, "}")
            }
        }
    }
}

val KSValueParameter.typeName: String
    get() {
        val ans = StringBuilder(type.resolve().declaration.qualifiedName?.asString() ?: "<ERROR>")
        val typeArgs = type.element!!.typeArguments
        if (typeArgs.isNotEmpty()) {
            ans.append("<")
            ans.append(
                typeArgs.joinToString(", ") {
                    val type = it.type?.resolve()
                    "${it.variance.label} ${type?.declaration?.qualifiedName?.asString() ?: "ERROR"}" +
                            if (type?.nullability == Nullability.NULLABLE) "?" else ""
                }
            )
            ans.append(">")
        }
        return ans.toString()
    }

fun OutputStream.appendText(indent: Int, str: String, newlines: Int = 1) {
    (" ".repeat(4 * indent) + str + "\n".repeat(newlines))
        .let { this.write(it.toByteArray()) }
}

fun OutputStream.newline() {
    this.write("\n".toByteArray())
}
