package embedded.koin.dsl

import embedded.koin.KoinCoreTest
import embedded.koin.Simple
import embedded.koin.core.component.KoinComponent
import embedded.koin.core.component.get
import embedded.koin.core.context.startKoin
import embedded.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ModuleRestartTest : KoinComponent, KoinCoreTest() {

    @BeforeTest
    fun before() {
        startKoin {
            modules(
                module {
                    single { Simple.ComponentA() }
                },
            )
        }
    }

    @Test
    fun first_test() {
        get<Simple.ComponentA>()
    }

    @Test
    fun second_test() {
        get<Simple.ComponentA>()
    }
}
