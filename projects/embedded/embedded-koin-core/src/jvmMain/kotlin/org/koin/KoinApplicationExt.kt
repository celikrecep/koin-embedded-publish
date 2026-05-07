package embedded.koin

import embedded.koin.core.KoinApplication
import embedded.koin.core.annotation.KoinInternalApi
import embedded.koin.core.registry.loadEnvironmentProperties
import embedded.koin.core.registry.loadPropertiesFromFile

/**
 * Load properties from file
 * @param fileName
 */
@OptIn(KoinInternalApi::class)
fun KoinApplication.fileProperties(fileName: String = "/koin.properties"): KoinApplication {
    koin.propertyRegistry.loadPropertiesFromFile(fileName)
    return this
}

/**
 * Load properties from environment
 */
@OptIn(KoinInternalApi::class)
fun KoinApplication.environmentProperties(): KoinApplication {
    koin.propertyRegistry.loadEnvironmentProperties()
    return this
}
