package embedded.koin.test.android.scope

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
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
import embedded.koin.androidx.viewmodel.ext.android.viewModel
import embedded.koin.core.annotation.KoinExperimentalAPI
import embedded.koin.core.component.getScopeId
import embedded.koin.core.context.startKoin
import embedded.koin.core.context.stopKoin
import embedded.koin.core.logger.Level
import embedded.koin.core.module.dsl.viewModel
import embedded.koin.core.option.viewModelScopeFactory
import embedded.koin.dsl.module
import embedded.koin.mp.KoinPlatform
import embedded.koin.test.android.scope.ScopeArchetypeDSLTest.MyFactoryClass
import embedded.koin.test.android.scope.ScopeArchetypeDSLTest.MyScopedClass
import embedded.koin.viewmodel.scope.viewModelScope
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

class ScopedVM(){
    val id : String = UUID.randomUUID().toString()
}
class FakeVM(val s : ScopedVM) : ViewModel()
class FakeVMActivity : ComponentActivity() {

    val fakeVM : FakeVM by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun useViewModel(){
        println("fakeVM :$fakeVM")
    }
}

@RunWith(RobolectricTestRunner::class)
class ViewModelScopeArchetypeTest {

    @OptIn(KoinExperimentalAPI::class)
    @Before
    fun setup() {
        startKoin {
            printLogger(Level.DEBUG)

            options(
                viewModelScopeFactory()
            )
        }
    }

    @After
    fun stop() {
        stopKoin()
    }

    @Test
    fun `viewModelScope resolves`() {
        val koin = KoinPlatform.getKoin()
        val module = module {
            viewModel { FakeVM(get()) }
            viewModelScope {
                scoped { ScopedVM() }
            }
        }
        koin.loadModules(listOf(module))

        val controller: ActivityController<FakeVMActivity> = Robolectric.buildActivity(FakeVMActivity::class.java)
        val activity: FakeVMActivity? = controller.get()
        assertNotNull(activity)
        controller.create().start().resume()

        activity.useViewModel()

        assertNotNull(activity.fakeVM)
    }
}
