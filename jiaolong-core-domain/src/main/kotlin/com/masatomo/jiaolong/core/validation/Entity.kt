package com.masatomo.jiaolong.core.validation

import com.masatomo.jiaolong.core.domain.DomainEntity
import com.masatomo.jiaolong.core.domain.InvalidDomainEntity
import kotlin.reflect.KClass


typealias AnnotatedEntityValidator<E> =
        Pair<KClass<out Annotation>, Annotation.(DomainEntity<E>) -> InvalidDomainEntity<E>?>

// Entity
interface InvalidDomainEntity<E : DomainEntity<E>> {
    val kClass: KClass<out E>
}

fun <E : DomainEntity<E>> DomainEntity<E>.validateBy(
    vararg validators: AnnotatedEntityValidator<E>
) = this::class.annotations.mapNotNull { ann ->
    validators.firstOrNull { it.first == ann.annotationClass }?.second?.invoke(ann, this)
}
