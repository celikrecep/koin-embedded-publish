package embedded.koin.koincomponent

import embedded.koin.KoinCoreTest
import embedded.koin.Simple
import embedded.koin.core.component.KoinComponent
import embedded.koin.core.component.get
import embedded.koin.core.component.inject
import embedded.koin.core.context.startKoin
import embedded.koin.core.context.stopKoin
import embedded.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MyComponent : KoinComponent {
    val anInject: Simple.ComponentA by inject()
    val aGet: Simple.ComponentA = get()
}

class MyLazyComponent : KoinComponent {
    val anInject: Simple.ComponentA by inject()
}

class KoinComponentTest : KoinCoreTest(){

    @Test
    fun can_lazy_inject_from_KoinComponent() {
        val app = startKoin {
            printLogger()
            modules(
                module {
                    single { Simple.ComponentA() }
                },
            )
        }

        val koin = app.koin
        val a: Simple.ComponentA = koin.get()
        val component = MyComponent()

        assertEquals(component.anInject, a)
        assertEquals(component.aGet, a)

        stopKoin()
    }

    @Test
    fun can_lazy_inject_before_starting_Koin() {
        var caughtException: Exception? = null
        var component: MyLazyComponent? = null
        try {
            component = MyLazyComponent()
        } catch (e: Exception) {
            caughtException = e
        }
        assertNull(caughtException)

        val app = startKoin {
            printLogger()
            modules(
                module {
                    single { Simple.ComponentA() }
                },
            )
        }

        val koin = app.koin
        val a: Simple.ComponentA = koin.get()
        assertEquals(component?.anInject, a)

        stopKoin()
    }
}
