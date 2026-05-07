package embedded.koin.core

import embedded.koin.core.error.NoDefinitionFoundException
import embedded.koin.core.logger.Level
import embedded.koin.core.qualifier.named
import embedded.koin.dsl.koinApplication
import embedded.koin.dsl.module
import kotlin.test.*

class GenericDeclarationTest {

    val modules = module {
        single(named("strings")) { listOf("a string") }
        single(named("ints")) { listOf(42) }
    }

    @Test
    fun `declare and retrieve generic definitions`() {
        val koin = createKoin()

        val aString = koin.get<List<String>>(named("strings"))
        assertEquals("a string", aString[0])

        val anInt = koin.get<List<Int>>(named("ints"))
        assertEquals(42, anInt[0])
    }

    @Test
    fun `declare and not retrieve generic definitions`() {
        val koin = createKoin()

        try {
            koin.get<List<String>>()
            fail()
        } catch (e: NoDefinitionFoundException) {
            assertNotNull(e)
        }
    }

    private fun createKoin(): Koin {
        val koin = koinApplication {
            printLogger(Level.DEBUG)
            modules(modules)
        }.koin
        return koin
    }
}
