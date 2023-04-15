package com.masatomo.jiaolong.core.repository.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.validate
import com.google.devtools.ksp.visitor.KSDefaultVisitor
import com.masatomo.jiaolong.core.ksp.ext.typeName
import com.masatomo.jiaolong.core.repository.GenerateRepository
import com.masatomo.jiaolong.core.repository.Repository
import com.masatomo.jiaolong.core.repository.RepositoryType
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MUTABLE_MAP
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.plusParameter
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import org.koin.ext.getFullName
import kotlin.reflect.KClass


interface OnMemory : RepositoryType

internal class OnMemoryRepositoryProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor = OnMemoryRepositoryProcessor(
        environment.codeGenerator,
        environment.logger,
    )
}

private class OnMemoryRepositoryProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val (targets, nonTarget) = resolver
            .getSymbolsWithAnnotation(GenerateRepository::class.getFullName())
            .partition { it.validate() }
        targets.asSequence()
            .filter { it is KSClassDeclaration }
            .filter { it.validate() }
            .forEach { it.accept(BuilderVisitor(), Unit) }
        return nonTarget
    }

    inner class BuilderVisitor : KSDefaultVisitor<Unit, Building>() {

        private lateinit var fileBuilder: FileSpec.Builder

        override fun defaultHandler(node: KSNode, data: Unit): Building {
            return Building.EMPTY
        }

        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit): Building {
            logger.info("visit class: $classDeclaration")

            val packageName = classDeclaration.containingFile!!.packageName.asString()
            val className = "AbstractOnMemory${classDeclaration.simpleName.asString()}"

            val entityType = classDeclaration.superTypes
                .first { it.resolve().declaration.qualifiedName!!.asString() == Repository::class.qualifiedName }
                .resolve().arguments[0]
            val idType = classDeclaration.superTypes
                .first { it.resolve().declaration.qualifiedName!!.asString() == Repository::class.qualifiedName }
                .resolve().arguments[1]

            FileSpec.builder(packageName, className)
                .also { fileBuilder = it }
                .addClass(className) {
                    abstract()
                    superInterface(classDeclaration.asStarProjectedType().toTypeName())
                    property("entities", MUTABLE_MAP.plusParameter(idType.toTypeName()).plusParameter(entityType.toTypeName())) {
                        initializer("mutableMapOf()")
                    }
                    classDeclaration.getAllFunctions()
                        .filter { it.isAbstract }
                        .map { it.accept(this@BuilderVisitor, data) }
                        .filter { it != Building.EMPTY }
                        .forEach { function(it.name, it.functionBuilder) }
                }
                .indent(" ".repeat(4))
                .build()
                .writeTo(codeGenerator, false)
            return Building.EMPTY
        }

        override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit): Building {
            logger.info("visit function: $function.")

            return when (function.simpleName.asString()) {
                "register" -> visitRegisterFunction(function, data)
                "findAll" -> visitFindAllFunction(function, data)
                "findById" -> visitFindByIdFunction(function, data)
                "update" -> visitUpdateFunction(function, data)
                "delete" -> visitDeleteFunction(function, data)
                else -> Building.EMPTY
            }
        }

        private fun visitRegisterFunction(function: KSFunctionDeclaration, data: Unit): Building {
            return Building(function.simpleName.asString(), functionBuilder = {
                if (function.isAbstract) addModifiers(KModifier.OVERRIDE)
                addAnnotation(Synchronized::class)
                function.parameters.forEach {
                    addParameter(it.name!!.asString(), it.type.toTypeName())
                }
                addCode(
                    CodeBlock.of(
                        """
                    return takeIf { entity.id == %T.UNASSIGNED }
                        ?.let { %T(entities.size.toLong()) }
                        ?.also { entities[it] = entity.assigned(it) }
                        ?: entity.id
                """.trimIndent(),
                        function.returnType!!.resolve().starProjection().toTypeName(),
                        function.returnType!!.toTypeName(),
                    )
                )
                function.returnType?.let { returns(it.toTypeName()) }
            })
        }

        private fun visitFindAllFunction(function: KSFunctionDeclaration, data: Unit): Building {
            return Building(function.simpleName.asString(), functionBuilder = {
                if (function.isAbstract) addModifiers(KModifier.OVERRIDE)
                function.parameters.forEach {
                    addParameter(it.name!!.asString(), it.type.toTypeName())
                }
                addCode(
                    CodeBlock.of(
                        """
                    return entities.values
                """.trimIndent()
                    )
                )
                function.returnType?.let { returns(it.toTypeName()) }
            })
        }

        private fun visitFindByIdFunction(function: KSFunctionDeclaration, data: Unit): Building {
            return Building(function.simpleName.asString(), functionBuilder = {
                if (function.isAbstract) addModifiers(KModifier.OVERRIDE)
                function.parameters.forEach {
                    addParameter(it.name!!.asString(), it.type.toTypeName())
                }
                addCode(
                    CodeBlock.of(
                        """
                    return entities[id]
                """.trimIndent()
                    )
                )
                function.returnType?.let { returns(it.toTypeName()) }
            })
        }

        private fun visitUpdateFunction(function: KSFunctionDeclaration, data: Unit): Building {
            return Building(function.simpleName.asString(), functionBuilder = {
                if (function.isAbstract) addModifiers(KModifier.OVERRIDE)
                addAnnotation(Synchronized::class)
                function.parameters.forEach {
                    addParameter(it.name!!.asString(), it.type.toTypeName())
                }
                addCode(
                    CodeBlock.of(
                        """
                    entities[entity.id] = entity
                """.trimIndent()
                    )
                )
                function.returnType?.let { returns(it.toTypeName()) }
            })
        }

        private fun visitDeleteFunction(function: KSFunctionDeclaration, data: Unit): Building {
            return Building(function.simpleName.asString(), functionBuilder = {
                if (function.isAbstract) addModifiers(KModifier.OVERRIDE)
                addAnnotation(Synchronized::class)
                function.parameters.forEach {
                    addParameter(it.name!!.asString(), it.type.toTypeName())
                }
                addCode(
                    CodeBlock.of(
                        """
                    entities.remove(id)
                """.trimIndent()
                    )
                )
                function.returnType?.let { returns(it.toTypeName()) }
            })
        }
    }
}

data class Building(
    val name: String,
    val functionBuilder: FunSpec.Builder.() -> Unit = { }
) {
    companion object {
        val EMPTY = Building("Empty")
    }
}


fun FileSpec.Builder.addClass(className: String, block: (TypeSpec.Builder).() -> Unit): FileSpec.Builder {
    return this.apply {
        addType(
            TypeSpec.classBuilder(className)
                .apply(block)
                .build()
        )
    }
}

fun TypeSpec.Builder.abstract() {
    addModifiers(KModifier.ABSTRACT)
}

fun TypeSpec.Builder.superInterface(superInterface: TypeName) {
    addSuperinterface(superInterface)
}

fun TypeSpec.Builder.property(propertyName: String, type: TypeName, block: PropertySpec.Builder.() -> Unit): TypeSpec.Builder {
    return this.apply {
        addProperty(
            PropertySpec.builder(propertyName, type)
                .apply(block)
                .build()
        )
    }
}

fun TypeSpec.Builder.function(functionName: String, block: FunSpec.Builder.() -> Unit): TypeSpec.Builder {
    return this.apply {
        addFunction(
            FunSpec.builder(functionName)
                .apply(block)
                .build()
        )
    }
}

fun KSClassDeclaration.findParameterType(annotationClass: KClass<*>, parameterName: String): KSType {
    return annotations
        .first { it.annotationType.typeName == annotationClass.qualifiedName }
        .arguments.first { it.name!!.asString() == parameterName }.value as KSType
}
