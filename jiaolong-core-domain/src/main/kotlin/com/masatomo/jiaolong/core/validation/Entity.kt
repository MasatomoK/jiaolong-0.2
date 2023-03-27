package com.masatomo.jiaolong.core.validation

import com.masatomo.jiaolong.core.domain.DomainEntity
import kotlin.reflect.KClass


interface InvalidDomainEntity {
    val kClass: KClass<out DomainEntity<*, *>>
}

data class AnnotatedEntityValidator<E : DomainEntity<E, *>>(
    val annClass: KClass<out Annotation>,
    val validation: Annotation.(E) -> InvalidDomainEntity?
)

@Suppress("UNCHECKED_CAST")
fun <A : Annotation, E : DomainEntity<E, *>> KClass<out A>.toValidator(
    validation: A.(E) -> InvalidDomainEntity?
) = AnnotatedEntityValidator<E>(this) { validation.invoke(this as A, it) }


fun <E : DomainEntity<E, *>> E.validateBy(
    vararg validators: AnnotatedEntityValidator<E>
) = this::class.annotations.mapNotNull { ann ->
    validators.firstOrNull { it.annClass == ann.annotationClass }
        ?.validation
        ?.invoke(ann, this)
}
