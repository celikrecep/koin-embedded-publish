package embedded.koin.test

import org.junit.Assert
import org.junit.Assert.assertNotEquals
import org.junit.Rule
import org.junit.Test
import embedded.koin.core.context.startKoin
import embedded.koin.core.logger.Level
import embedded.koin.dsl.module
import embedded.koin.test.mock.MockProviderRule
import embedded.koin.test.mock.declareMock
import org.mockito.BDDMockito
import org.mockito.Mockito
import java.util.*

class DeclareMockFromKoinTest : AutoCloseKoinTest() {

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

    val mock: Simple.UUIDComponent by inject()

    @Test
    fun `declareMock with KoinTest`() {
        startKoin {
            printLogger(Level.DEBUG)
            modules(
                module {
                    single { Simple.UUIDComponent() }
                },
            )
        }

        val uuidValue = "UUID"
        declareMock<Simple.UUIDComponent> {
            BDDMockito.given(getUUID()).will { uuidValue }
        }

        Assert.assertEquals(uuidValue, mock.getUUID())
    }

    @Test
    fun `declareMock factory with KoinTest`() {
        startKoin {
            printLogger(Level.DEBUG)
            modules(
                module {
                    factory { Simple.UUIDComponent() }
                },
            )
        }

        declareMock<Simple.UUIDComponent> {
            BDDMockito.given(getUUID()).will { UUID.randomUUID().toString() }
        }

        val val1 = getKoin().get<Simple.UUIDComponent>().getUUID()
        val val2 = getKoin().get<Simple.UUIDComponent>().getUUID()

        assertNotEquals(val1, val2)
    }
}
