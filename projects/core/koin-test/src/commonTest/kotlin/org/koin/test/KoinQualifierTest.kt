package embedded.koin.test

import embedded.koin.core.context.startKoin
import embedded.koin.core.context.stopKoin
import embedded.koin.core.qualifier.named
import embedded.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertNotNull

class KoinQualifierTest : KoinTest {

    val qualifier = named("42")
    val myString42: Simple.MyString by inject(qualifier)

    @Test
    fun use_0qualifier() {
        startKoin {
            modules(
                module {
                    single(qualifier) { Simple.MyString("42") }
                },
            )
        }

        assertNotNull(myString42)

        stopKoin()
    }
}
