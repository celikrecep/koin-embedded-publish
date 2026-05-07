import org.junit.After
import embedded.koin.core.context.stopKoin
import kotlin.test.Test
import kotlin.test.fail
import embedded.koin.core.error.InstanceCreationException
import embedded.koin.dsl.module
import embedded.koin.test.Simple
import embedded.koin.test.check.checkKoinModules
import embedded.koin.test.verify.MissingKoinDefinitionException

class CheckModulesTest {

    @Test
    fun verify_one_simple_module() {
        val module = module {
            single { Simple.ComponentA() }
            single { Simple.ComponentB(get()) }
        }

        try {
            checkKoinModules(listOf(module))
        } catch (e: MissingKoinDefinitionException) {
            fail("Should not fail to verify module - $e")
        }
    }

    @Test(expected = InstanceCreationException::class)
    fun verify_one_simple_broken_module() {
        val module = module {
            single { Simple.ComponentB(get()) }
        }

        checkKoinModules(listOf(module))
        fail("Should fail to verify module")
    }

    @After
    fun after(){
        stopKoin()
    }
}
