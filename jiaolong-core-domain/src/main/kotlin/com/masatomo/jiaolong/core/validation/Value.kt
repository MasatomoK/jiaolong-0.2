package com.masatomo.jiaolong.core.validation

import com.masatomo.jiaolong.core.domain.DomainValue
import kotlin.reflect.KClass


interface InvalidDomainValue<V : DomainValue<V>> {
    val kClass: KClass<out V>
}


typealias AnnotatedValueValidator<V> =
        Pair<KClass<out Annotation>, Annotation.(DomainValue<V>) -> InvalidDomainValue<V>?>

typealias CustomValueValidator<V> = (DomainValue<V>) -> InvalidDomainValue<V>?


fun <V : DomainValue<V>> DomainValue<V>.validateBy(
    annotatedValidators: Array<AnnotatedValueValidator<V>>,
    customValidators: Array<CustomValueValidator<V>>,
) = listOf(*validateBy(*annotatedValidators).toTypedArray(), *validateBy(*customValidators).toTypedArray())

fun <V : DomainValue<V>> DomainValue<V>.validateBy(
    vararg validators: AnnotatedValueValidator<V>
) = this::class.annotations.mapNotNull { ann ->
    validators.firstOrNull { it.first == ann.annotationClass }?.second?.invoke(ann, this)
}

fun <V : DomainValue<V>> DomainValue<V>.validateBy(
    vararg validators: CustomValueValidator<V>
) = validators.mapNotNull { it.invoke(this) }
