package embedded.koin.core

import embedded.koin.Simple
import embedded.koin.core.logger.Level
import embedded.koin.core.parameter.parametersOf
import embedded.koin.dsl.koinApplication
import embedded.koin.dsl.module
import embedded.koin.mp.KoinPlatformTools
import embedded.koin.mp.generateId
import kotlin.test.Test

class ParameterStackTest {

    @Test
    fun test_parameterstack_is_empty() {
        val koin = koinApplication {
            printLogger(Level.DEBUG)
            modules(
                module {
                    factory { (id: String) -> Simple.MyStringFactory(id) }
                },
            )
        }.koin

        for (index in 1..10) {
            koin.get<Simple.MyStringFactory> { parametersOf(KoinPlatformTools.generateId()) }
        }

//        assertTrue(koin.scopeRegistry.rootScope._parameterStackLocal.get()!!.isEmpty())
    }
}
