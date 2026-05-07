package embedded.koin.test

import embedded.koin.core.context.loadKoinModules
import embedded.koin.core.context.startKoin
import embedded.koin.core.context.stopKoin
import embedded.koin.core.logger.Level
import embedded.koin.dsl.module
import kotlin.test.Test
import kotlin.test.fail

class DeclareTest : KoinTest {

    @Test
    fun declare_on_the_fly_with_KoinTest() {
        startKoin {
            printLogger(Level.DEBUG)
        }

        try {
            get<Simple.ComponentA>()
            fail()
        } catch (e: Exception) {
        }

        loadKoinModules(
            module {
                single { Simple.ComponentA() }
            },
        )

        get<Simple.ComponentA>()

        stopKoin()
    }
}
