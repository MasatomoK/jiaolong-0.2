package com.masatomo.jiaolong.core.ksp.ext

import com.google.devtools.ksp.symbol.KSTypeReference
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.symbol.Nullability


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

val KSTypeReference.typeName: String
    get() {
        val ans = StringBuilder(resolve().declaration.qualifiedName?.asString() ?: "<ERROR>")
        val typeArgs = element!!.typeArguments
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
