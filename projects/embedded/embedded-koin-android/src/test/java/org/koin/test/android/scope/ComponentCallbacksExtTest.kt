package embedded.koin.test.android.scope

import android.content.ComponentCallbacks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import embedded.koin.android.ext.android.getKoin
import embedded.koin.android.scope.createScope
import embedded.koin.android.scope.getScopeOrNull
import embedded.koin.core.KoinApplication
import embedded.koin.core.context.startKoin
import embedded.koin.core.context.stopKoin
import embedded.koin.core.scope.Scope
import embedded.koin.test.KoinTest
import embedded.koin.test.android.helper.Helper.componentCallbacks

class ComponentCallbacksExtTest : KoinTest {

    private val componentCallbackExt = "embedded.koin.android.ext.android.ComponentCallbackExtKt"

    private val scope: Scope = mockk(relaxed = true)

    private lateinit var app: KoinApplication

    @Before
    fun before() {
        app = KoinApplication.init()
        startKoin(app)
        mockkStatic(componentCallbackExt)
    }

    @After
    fun after() {
        unmockkStatic(componentCallbackExt)
        stopKoin()
    }

    @Test
    fun `GIVEN component callbacks with mocked scope WHEN create scope THEN get mocked value`() {
        // GIVEN
        every { any<ComponentCallbacks>().getKoin().createScope(any(), any(), any()) } returns scope

        // WHEN
        val result = componentCallbacks.createScope<ComponentCallbacks>()

        // THEN
        Assert.assertEquals(scope, result)
    }

    @Test
    fun `GIVEN component callbacks with mocked scope WHEN get scope or null THEN get mocked value`() {
        // GIVEN
        every { any<ComponentCallbacks>().getKoin().getScopeOrNull(any()) } returns scope

        // WHEN
        val result = componentCallbacks.getScopeOrNull<ComponentCallbacks>()

        // THEN
        Assert.assertEquals(scope, result)
    }
}
