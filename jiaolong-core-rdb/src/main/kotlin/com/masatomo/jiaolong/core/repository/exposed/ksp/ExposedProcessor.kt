package com.masatomo.jiaolong.core.repository.exposed.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.validate
import com.google.devtools.ksp.visitor.KSDefaultVisitor
import com.masatomo.jiaolong.core.domain.DomainEntity
import com.masatomo.jiaolong.core.domain.values.Id
import com.masatomo.jiaolong.core.ksp.ext.typeName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import org.jetbrains.exposed.dao.id.LongIdTable
import org.koin.ext.getFullName
import kotlin.reflect.KClass

annotation class GenerateExposedTable

interface TableSeed<E : DomainEntity<E, I>, I : Id<E>>

annotation class GenerateAbstractExposedRepository

internal class ExposedRepositoryProvider : SymbolProcessorProvider {
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
            .getSymbolsWithAnnotation(GenerateExposedTable::class.getFullName())
            .partition { it.validate() }
        targets.asSequence()
            .filterIsInstance<KSClassDeclaration>()
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

            val entityType = classDeclaration.superTypes
                .first { it.resolve().declaration.qualifiedName!!.asString() == TableSeed::class.qualifiedName }
                .resolve().arguments[0]
            val entitySimplexName = entityType.toTypeName().toString().split(".").last()
            val idType = classDeclaration.superTypes
                .first { it.resolve().declaration.qualifiedName!!.asString() == TableSeed::class.qualifiedName }
                .resolve().arguments[1]

            val packageName = classDeclaration.containingFile!!.packageName.asString()
            val className = "Generated${entitySimplexName}Table"

            FileSpec.builder(packageName, className)
                .also { fileBuilder = it }
                .addObject(className) {
                    superclass(LongIdTable::class)
                    addSuperclassConstructorParameter("name = %S", entitySimplexName)
                    entityType.type!!.resolve().declaration
                        .let { it as KSClassDeclaration }
                        .getAllProperties()
                        .forEach {
                            logger.info(it.toString())
                        }
                }
                .indent(" ".repeat(4))
                .build()
                .writeTo(codeGenerator, false)
            return Building.EMPTY
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

fun FileSpec.Builder.addObject(className: String, block: (TypeSpec.Builder).() -> Unit): FileSpec.Builder {
    return this.apply {
        addType(
            TypeSpec.objectBuilder(className)
                .apply(block)
                .build()
        )
    }
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
