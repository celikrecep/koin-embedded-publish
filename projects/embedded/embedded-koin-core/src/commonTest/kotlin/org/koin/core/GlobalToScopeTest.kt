package embedded.koin.core

import embedded.koin.Simple
import embedded.koin.core.error.NoDefinitionFoundException
import embedded.koin.core.logger.Level
import embedded.koin.core.qualifier.named
import embedded.koin.dsl.koinApplication
import embedded.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class GlobalToScopeTest {

    @Test
    fun `can't get scoped dependency without scope`() {
        val koin = koinApplication {
            printLogger(Level.DEBUG)
            modules(
                module {
                    scope(named<ClosedScopeAPI.ScopeType>()) {
                        scoped { Simple.ComponentA() }
                    }
                },
            )
        }.koin

        try {
            koin.get<Simple.ComponentA>()
            fail()
        } catch (e: NoDefinitionFoundException) {
            e.printStackTrace()
        }
    }

    @Test
    fun `can't get scoped dependency without scope from single`() {
        val koin = koinApplication {
            printLogger(Level.DEBUG)
            modules(
                module {
                    single { Simple.ComponentB(get()) }

                    scope(named<ClosedScopeAPI.ScopeType>()) {
                        scoped { Simple.ComponentA() }
                    }
                },
            )
        }.koin

        try {
            koin.get<Simple.ComponentA>()
            fail()
        } catch (e: NoDefinitionFoundException) {
            e.printStackTrace()
        }
    }

    @Test
    fun `get scoped dependency without scope from single`() {
        val scopeId = "MY_SCOPE_ID"

        val koin = koinApplication {
            printLogger(Level.DEBUG)
            modules(
                module {
                    single { Simple.ComponentB(getScope(scopeId).get()) }

                    scope(named<ClosedScopeAPI.ScopeType>()) {
                        scoped { Simple.ComponentA() }
                    }
                },
            )
        }.koin

        val scope = koin.createScope(scopeId, named<ClosedScopeAPI.ScopeType>())
        assertEquals(koin.get<Simple.ComponentB>().a, scope.get<Simple.ComponentA>())
    }
}
