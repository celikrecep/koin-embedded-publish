@file:OptIn(KoinInternalApi::class, KoinInternalApi::class)

package embedded.koin.core.module

import embedded.koin.core.annotation.KoinInternalApi
import embedded.koin.core.definition.BeanDefinition
import embedded.koin.core.definition.Definition
import embedded.koin.core.definition.Kind
import embedded.koin.core.definition.KoinDefinition
import embedded.koin.core.instance.FactoryInstanceFactory
import embedded.koin.core.instance.SingleInstanceFactory
import embedded.koin.core.qualifier.Qualifier
import embedded.koin.core.registry.ScopeRegistry
import kotlin.reflect.KClass

/**
 * Module extension for Native to allow use of Module API without inlined KClass Type
 *
 * @author Arnaud Giuliani
 */

internal fun <T : Any> Module.createDefinition(
    kind: Kind = Kind.Singleton,
    kClass: KClass<T>,
    qualifier: Qualifier? = null,
    definition: Definition<T>,
    secondaryTypes: List<KClass<*>> = emptyList(),
    scopeQualifier: Qualifier = ScopeRegistry.rootScopeQualifier,
): BeanDefinition<T> {
    return BeanDefinition(
        scopeQualifier,
        kClass,
        qualifier,
        definition,
        kind,
        secondaryTypes = secondaryTypes,
    )
}

/**
 * Create a factory
 */
fun <T : Any> Module.factory(
    kClass: KClass<T>,
    qualifier: Qualifier? = null,
    definition: Definition<T>,
    scopeQualifier: Qualifier = ScopeRegistry.rootScopeQualifier,
): KoinDefinition<T> {
    val def = createDefinition(Kind.Factory, kClass, qualifier, definition, scopeQualifier = scopeQualifier)
    val factory = FactoryInstanceFactory(def)
    indexPrimaryType(factory)
    return KoinDefinition(this, factory)
}

/**
 * Create a Single
 */
fun <T : Any> Module.single(
    kClass: KClass<T>,
    qualifier: Qualifier? = null,
    definition: Definition<T>,
    createdAtStart: Boolean = false,
    scopeQualifier: Qualifier = ScopeRegistry.rootScopeQualifier,
): KoinDefinition<T> {
    val def = createDefinition(Kind.Singleton, kClass, qualifier, definition, scopeQualifier = scopeQualifier)
    val factory = SingleInstanceFactory(def)
    indexPrimaryType(factory)
    if (createdAtStart) {
        prepareForCreationAtStart(factory)
    }
    return KoinDefinition(this, factory)
}
