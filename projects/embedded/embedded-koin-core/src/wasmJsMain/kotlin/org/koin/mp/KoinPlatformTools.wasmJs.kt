package embedded.koin.mp

import embedded.koin.core.annotation.KoinExperimentalAPI
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@KoinExperimentalAPI
actual fun KoinPlatformTools.generateId(): String = Uuid.random().toString()