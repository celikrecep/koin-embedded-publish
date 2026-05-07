package embedded.koin.core

import embedded.koin.Simple
import embedded.koin.core.logger.Level
import embedded.koin.core.qualifier.named
import embedded.koin.dsl.koinApplication
import embedded.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertEquals

class ScopeShadowingTest {

    @Test
    fun `can't get scoped dependency without scope from single`() {
        val koin = koinApplication {
            printLogger(Level.DEBUG)
            modules(
                module {
                    single { Simple.MySingle(24) }

                    scope(named<ClosedScopeAPI.ScopeType>()) {
                        scoped { Simple.MySingle(42) }
                    }
                },
            )
        }.koin

        val scope = koin.createScope("scope", named<ClosedScopeAPI.ScopeType>())
        assertEquals(42, scope.get<Simple.MySingle>().id)

        assertEquals(24, koin.get<Simple.MySingle>().id)
    }
}
