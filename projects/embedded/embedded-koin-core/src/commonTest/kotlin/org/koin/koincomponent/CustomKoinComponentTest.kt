package embedded.koin.koincomponent

import embedded.koin.KoinCoreTest
import embedded.koin.Simple
import embedded.koin.core.Koin
import embedded.koin.core.component.KoinComponent
import embedded.koin.core.component.inject
import embedded.koin.dsl.koinApplication
import embedded.koin.dsl.module
import embedded.koin.test.assertHasNoStandaloneInstance
import kotlin.native.concurrent.ThreadLocal
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class CustomKoinComponent : KoinComponent {

    override fun getKoin(): Koin = customKoin

    @ThreadLocal
    companion object {
        val customKoin = koinApplication {
            modules(
                module {
                    single { Simple.ComponentA() }
                },
            )
        }.koin
    }
}

class MyCustomApp : CustomKoinComponent() {
    val a: Simple.ComponentA by inject()
}

class CustomKoinComponentTest : KoinCoreTest(){

    @Test
    fun can_inject_KoinComponent_from_custom_instance() {
        val app = MyCustomApp()
        val a = CustomKoinComponent.customKoin.get<Simple.ComponentA>()

        assertEquals(app.a, a)

        assertHasNoStandaloneInstance()
    }
}
