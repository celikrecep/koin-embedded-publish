package embedded.koin.core

import embedded.koin.Simple
import embedded.koin.core.logger.Level
import embedded.koin.core.module.dsl.scopedOf
import embedded.koin.core.parameter.parametersOf
import embedded.koin.dsl.koinApplication
import embedded.koin.dsl.module
import kotlin.test.Test

class ScopeInfoTest {

    @Test
    fun resolve_via_a_scope(){

        val koin = koinApplication {
            printLogger(Level.DEBUG)
            modules(
                module {
                    factory { Simple.ComponentC(get()) }
                    single { Simple.ComponentA() }
                    scope<Simple.ComponentA> {
                        scopedOf(Simple::ComponentB)
                    }
                }
            )
        }.koin

        val a = koin.get<Simple.ComponentA>()
        val scope = koin.createScope<Simple.ComponentA>("_a_",a)
        val b = scope.get<Simple.ComponentB>()
        scope.get<Simple.ComponentC> { parametersOf(b) }
    }
}