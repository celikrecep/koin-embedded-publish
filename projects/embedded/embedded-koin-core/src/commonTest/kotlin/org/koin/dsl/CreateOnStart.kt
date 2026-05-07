package embedded.koin.dsl

import embedded.koin.KoinCoreTest
import embedded.koin.Simple
import embedded.koin.core.component.KoinComponent
import embedded.koin.core.context.startKoin
import embedded.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CreateOnStart : KoinCoreTest() {

    @Test
    fun `works with koin 2_2_3 and koin 3_0_2 breaks with koin 3_1_4`() {
        startKoin {
            modules(
                module {
                    single(createdAtStart = true) { InjectionTarget() }
                },
            )
            properties(
                mapOf(
                    "foo" to "bar",
                ),
            )
        }
    }

    @Test
    fun `works with all versions of koin`() {
        startKoin {
            properties(
                mapOf(
                    "foo" to "bar",
                ),
            )
            modules(
                module {
                    single(createdAtStart = true) { InjectionTarget() }
                },
            )
        }
    }

    @Test
    fun `createdAt start`() {
        stopKoin()
        var created = 0
        startKoin {
            modules(
                module {
                    single(createdAtStart = true) {
                        Simple.ComponentA()
                        created++
                    }
                },
            )
        }

        assertTrue(created == 1)
    }

    @Test
    fun `createdAt start - koinApp`() {
        var created = 0
        koinApplication {
            modules(
                module {
                    single(createdAtStart = true) {
                        Simple.ComponentA()
                        created++
                    }
                },
            )
        }

        assertTrue(created == 1)
    }

    class InjectionTarget : KoinComponent {
        init {
            assertEquals("bar", getKoin().getProperty<String>("foo"))
        }
    }
}
