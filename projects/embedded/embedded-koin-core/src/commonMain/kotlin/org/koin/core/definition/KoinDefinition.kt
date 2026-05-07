package embedded.koin.core.definition

import embedded.koin.core.instance.InstanceFactory
import embedded.koin.core.module.KoinDslMarker
import embedded.koin.core.module.Module

@KoinDslMarker
data class KoinDefinition<R>(val module: Module, val factory: InstanceFactory<R>)
