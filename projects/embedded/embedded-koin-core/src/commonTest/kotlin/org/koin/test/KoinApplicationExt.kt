package embedded.koin.test

import embedded.koin.core.KoinApplication
import embedded.koin.core.annotation.KoinInternalApi
import embedded.koin.core.definition.BeanDefinition
import embedded.koin.core.instance.InstanceFactory
import embedded.koin.core.scope.Scope
import kotlin.reflect.KClass
import kotlin.test.assertEquals

@OptIn(KoinInternalApi::class)
fun KoinApplication.assertDefinitionsCount(count: Int) {
    assertEquals(count, this.koin.instanceRegistry.size(), "definitions count")
}

@OptIn(KoinInternalApi::class)
internal fun KoinApplication.getBeanDefinition(clazz: KClass<*>): BeanDefinition<*>? {
    return this.koin.scopeRegistry.rootScope.getBeanDefinition(clazz)
}

@OptIn(KoinInternalApi::class)
internal fun Scope.getBeanDefinition(clazz: KClass<*>): BeanDefinition<*>? {
    return _koin.instanceRegistry.instances.values.firstOrNull { it.beanDefinition.primaryType == clazz }?.beanDefinition
}

@OptIn(KoinInternalApi::class)
internal fun KoinApplication.getInstanceFactory(clazz: KClass<*>): InstanceFactory<*>? {
    return this.koin.instanceRegistry.instances.values.firstOrNull { it.beanDefinition.primaryType == clazz }
}
