package embedded.koin.core.scope

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import embedded.koin.android.scope.AndroidScopeComponent
import embedded.koin.androidx.scope.ActivityRetainedScopeArchetype
import embedded.koin.androidx.scope.ActivityScopeArchetype
import embedded.koin.androidx.scope.FragmentScopeArchetype
import embedded.koin.androidx.scope.activityRetainedScope
import embedded.koin.androidx.scope.activityScope
import embedded.koin.androidx.scope.dsl.activityRetainedScope
import embedded.koin.androidx.scope.dsl.activityScope
import embedded.koin.androidx.scope.dsl.fragmentScope
import embedded.koin.androidx.scope.fragmentScope
import embedded.koin.core.annotation.KoinInternalApi
import embedded.koin.core.component.getScopeId
import embedded.koin.core.context.startKoin
import embedded.koin.core.context.stopKoin
import embedded.koin.core.logger.Level
import embedded.koin.dsl.module
import embedded.koin.mp.KoinPlatform
import embedded.koin.test.android.scope.ScopeArchetypeDSLTest.MyFactoryClass
import embedded.koin.test.android.scope.ScopeArchetypeDSLTest.MyScopedClass
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame


class FakeActivity : ComponentActivity(), AndroidScopeComponent {

    override val scope by activityScope()
}

class FakeFragment : Fragment(), AndroidScopeComponent {

    override val scope by fragmentScope()
}

class FakeRetainedActivity : ComponentActivity(), AndroidScopeComponent {

    override val scope by activityRetainedScope()
}

@RunWith(RobolectricTestRunner::class)
class ActivityScopeArchetypeTest {

    @Before
    fun setup() {
        startKoin { printLogger(Level.DEBUG) }
    }

    @After
    fun stop() {
        stopKoin()
    }

    @Test
    fun `activityScope creates scope with ActivityScopeArchetype`() {
        val koin = KoinPlatform.getKoin()
        val activity = FakeActivity()

        val scope = activity.scope

        assertSame(koin.getScope(activity.getScopeId()), scope)
        assert(scope.scopeArchetype == ActivityScopeArchetype)
    }

    @Test
    fun `activityScope resolve scope with ActivityScopeArchetype`() {
        val koin = KoinPlatform.getKoin()
        val module = module {
            activityScope {
                scoped { MyScopedClass() }
                factory { MyFactoryClass(get()) }
            }
        }
        koin.loadModules(listOf(module))

        val activity = FakeActivity()

        val scope = activity.scope
        val mf = scope.get<MyFactoryClass>()
        assertEquals(mf.ms, scope.get<MyScopedClass>())
    }

    @Test
    fun `fragmentScope creates scope with FragmentScopeArchetype`() {
        val koin = KoinPlatform.getKoin()
        val fragment = FakeFragment()

        val scope = fragment.scope

        assertSame(koin.getScope(fragment.getScopeId()), scope)
        assert(scope.scopeArchetype == FragmentScopeArchetype)
    }

    @Test
    fun `fragmentScope resolves scope with FragmentScopeArchetype`() {
        val koin = KoinPlatform.getKoin()
        val module = module {
            fragmentScope {
                scoped { MyScopedClass() }
                factory { MyFactoryClass(get()) }
            }
        }
        koin.loadModules(listOf(module))

        val fragment = FakeFragment()

        val scope = fragment.scope
        val mf = scope.get<MyFactoryClass>()
        assertEquals(mf.ms, scope.get<MyScopedClass>())
    }

    @Test
    fun `fragmentScope resolves scope with FragmentScopeArchetype - cascade activity scope`() {
        val koin = KoinPlatform.getKoin()
        val module = module {
            activityScope {
                scoped { MyScopedClass() }
            }
            fragmentScope {
                factory { MyFactoryClass(get()) }
            }
        }
        koin.loadModules(listOf(module))

        val activity = FakeActivity()
        val fragment = FakeFragment()

        val activityScope = activity.scope
        val fragmentScope = fragment.scope

        // fake by fragmentScope -> activityScope
        fragmentScope.linkTo(activityScope)

        val mf = fragmentScope.get<MyFactoryClass>()
        assertEquals(mf.ms, activityScope.get<MyScopedClass>())
    }

    @Test
    fun `activityRetainedScope creates scope with RetainedActivityScopeArchetype`() {

        val controller = Robolectric.buildActivity(FakeRetainedActivity::class.java).setup()
        val activity = controller.get()

        val koin = KoinPlatform.getKoin()

        val scope = activity.activityRetainedScope().value

        assertSame(koin.getScope(activity.getScopeId()), scope)
        assert(scope.scopeArchetype == ActivityRetainedScopeArchetype)
    }

    @Test
    fun `activityRetainedScope resolves scope with RetainedActivityScopeArchetype`() {

        val controller = Robolectric.buildActivity(FakeRetainedActivity::class.java).setup()
        val activity = controller.get()

        val koin = KoinPlatform.getKoin()
        val module = module {
            activityRetainedScope {
                scoped { MyScopedClass() }
                factory { MyFactoryClass(get()) }
            }
        }
        koin.loadModules(listOf(module))

        val scope = activity.activityRetainedScope().value
        val mf = scope.get<MyFactoryClass>()
        assertEquals(mf.ms, scope.get<MyScopedClass>())
    }

    @Test
    fun `wrong activity scope`() {
        val controller = Robolectric.buildActivity(FakeRetainedActivity::class.java).setup()
        val activity = controller.get()

        val koin = KoinPlatform.getKoin()
        val module = module {
            activityScope {
                scoped { MyScopedClass() }
                factory { MyFactoryClass(get()) }
            }
        }
        koin.loadModules(listOf(module))

        val scope = activity.activityRetainedScope().value
        val mf = scope.getOrNull<MyFactoryClass>()
        assertNull(mf)
    }

    @OptIn(KoinInternalApi::class)
    @Test
    fun `scope resolution should work with single linked scope - GitHub Issue 2221 regression test`() {
        // This test verifies that the CoreResolver fix on line 131 works correctly
        // The issue was caused by always calling flatten() instead of conditional flattening
        val koin = KoinPlatform.getKoin()
        val testModule = module {
            single<String> { "root-value" }
            activityScope {
                scoped<MyScopedClass> { MyScopedClass() }
                factory<MyFactoryClass> { 
                    // This should resolve from root scope without throwing ClosedScopeException
                    MyFactoryClass(get()) 
                }
            }
        }
        koin.loadModules(listOf(testModule))
        
        val activity = FakeActivity()
        val activityScope = activity.scope
        
        // Verify the activity scope has only one linked scope (root)
        assertEquals(1, activityScope.getLinkedScopeIds().size)
        assertEquals("_root_", activityScope.getLinkedScopeIds().first())
        
        // This should work without ClosedScopeException due to the CoreResolver fix
        val factory = activityScope.get<MyFactoryClass>()
        val scoped = activityScope.get<MyScopedClass>()
        
        // Verify resolution worked correctly
        assertEquals(scoped, factory.ms)
        
        // Verify root scope resolution still works
        val rootValue = activityScope.get<String>()
        assertEquals("root-value", rootValue)
    }
}
