package embedded.koin.compose

import androidx.compose.runtime.Composable
import embedded.koin.core.Koin
import embedded.koin.core.KoinApplication
import embedded.koin.core.annotation.KoinExperimentalAPI
import embedded.koin.dsl.KoinConfiguration
import embedded.koin.core.logger.Level
import embedded.koin.dsl.koinConfiguration
import embedded.koin.dsl.includes
import embedded.koin.mp.KoinPlatform

@Composable
@KoinExperimentalAPI
internal actual fun composeMultiplatformConfiguration(loggerLevel : Level, config : KoinConfiguration) : KoinConfiguration {
    return koinConfiguration {
        printLogger(loggerLevel)
        includes(config)
    }
}

@Composable
internal actual fun retrieveDefaultInstance() : Koin {
    return KoinPlatform.getKoin()
}