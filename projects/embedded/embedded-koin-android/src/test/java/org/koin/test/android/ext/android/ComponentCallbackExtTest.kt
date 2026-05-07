package embedded.koin.test.android.ext.android

import android.content.ComponentCallbacks
import io.mockk.mockk
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import embedded.koin.android.ext.android.get
import embedded.koin.android.ext.android.getKoin
import embedded.koin.android.ext.android.inject
import embedded.koin.core.KoinApplication
import embedded.koin.core.context.startKoin
import embedded.koin.core.context.stopKoin
import embedded.koin.core.qualifier.named
import embedded.koin.test.KoinTest
import embedded.koin.test.android.helper.FakeContent
import embedded.koin.test.android.helper.Helper
import embedded.koin.test.android.helper.Helper.componentCallbacks
import embedded.koin.test.android.helper.Helper.koinComponent

class ComponentCallbackExtTest : KoinTest {

    private lateinit var app: KoinApplication

    @Before
    fun before() {
        app = KoinApplication.init()
        startKoin(app)
        app.modules(Helper.module)
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun `GIVEN KoinApplication initialized WHEN get koin from ComponentCallbacks THEN get koin same Koin instance from KoinComponent`() {
        // WHEN
        val result = (koinComponent as ComponentCallbacks).getKoin()

        // THEN
        Assert.assertEquals(app.koin, result)
    }

    @Test
    fun `GIVEN ComponentCallbacks WHEN get koin THEN retrieve koin from global context`() {
        // GIVEN
        mockk<ComponentCallbacks>(relaxed = true) {

            // WHEN
            val koin = getKoin()

            // THEN
            Assert.assertEquals(app.koin, koin)
        }
    }

    @Test
    fun `GIVEN module started in koin application WHEN inject THEN get expected data`() {
        // WHEN
        val qualified: Lazy<FakeContent> = componentCallbacks.inject(qualifier = named("custom"))
        val unqualified: Lazy<FakeContent> = componentCallbacks.inject()

        // THEN
        Assert.assertNotNull(qualified.value)
        Assert.assertNotNull(unqualified.value)
    }

    @Test
    fun `GIVEN module started in koin application WHEN get THEN get expected data`() {
        // WHEN
        val qualified = componentCallbacks.get<FakeContent>(qualifier = named("custom"))
        val unqualified = componentCallbacks.get<FakeContent>()

        // THEN
        Assert.assertNotNull(qualified)
        Assert.assertNotNull(unqualified)
    }
}
