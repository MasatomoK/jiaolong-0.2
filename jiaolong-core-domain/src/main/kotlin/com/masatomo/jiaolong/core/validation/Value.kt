package com.masatomo.jiaolong.core.validation

import com.masatomo.jiaolong.core.domain.DomainValue
import kotlin.reflect.KClass


interface InvalidDomainValue {
    val kClass: KClass<out DomainValue>
}


data class AnnotatedValueValidator<in V : DomainValue>(
    val annClass: KClass<out Annotation>,
    val validation: Annotation.(V) -> InvalidDomainValue?
)

@Suppress("UNCHECKED_CAST")
fun <A : Annotation, V : DomainValue> KClass<out A>.toValidator(
    validation: A.(V) -> InvalidDomainValue?
) = AnnotatedValueValidator<V>(this) { validation.invoke(this as A, it) }

fun <V : DomainValue> V.validateBy(
    vararg validators: AnnotatedValueValidator<V>
) = this::class.annotations.mapNotNull { ann ->
    validators.firstOrNull { it.annClass == ann.annotationClass }
        ?.validation
        ?.invoke(ann, this)
}
