package embedded.koin.core

import embedded.koin.Simple
import embedded.koin.core.qualifier.named
import embedded.koin.dsl.koinApplication
import embedded.koin.dsl.module
import embedded.koin.test.assertDefinitionsCount
import kotlin.test.Test
import kotlin.test.assertTrue

class DefinitionOverrideTest {

    @Test
    fun `allow overrides by type`() {
        val app = koinApplication {
            modules(
                module {
                    single<Simple.ComponentInterface1> { Simple.Component2() }
                },
                module {
                    single<Simple.ComponentInterface1> { Simple.Component1() }
                },
            )
        }

        app.assertDefinitionsCount(1)
        assertTrue(app.koin.get<Simple.ComponentInterface1>() is Simple.Component1)
    }

    @Test
    fun `allow overrides by type - scope`() {
        val app = koinApplication {
            modules(
                module {
                    scope<Simple.ComponentA> {
                        scoped<Simple.ComponentInterface1> { Simple.Component2() }
                    }
                },
                module {
                    scope<Simple.ComponentA> {
                        scoped<Simple.ComponentInterface1> { Simple.Component1() }
                    }
                },
            )
        }
        val scope = app.koin.createScope<Simple.ComponentA>("_ID_")
        assertTrue(scope.get<Simple.ComponentInterface1>() is Simple.Component1)
    }

    @Test
    fun `allow overrides by name`() {
        val app = koinApplication {
            modules(
                module {
                    single<Simple.ComponentInterface1>(named("DEF")) { Simple.Component2() }
                },
                module {
                    single<Simple.ComponentInterface1>(named("DEF")) { Simple.Component1() }
                },
            )
        }

        app.assertDefinitionsCount(1)
        assertTrue(app.koin.get<Simple.ComponentInterface1>(named("DEF")) is Simple.Component1)
    }
}
