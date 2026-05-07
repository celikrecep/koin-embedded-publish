package embedded.koin.test.android

import androidx.lifecycle.ViewModel
import org.junit.Test
import embedded.koin.androidx.viewmodel.dsl.viewModelOf
import embedded.koin.core.module.dsl.factoryOf
import embedded.koin.core.module.dsl.named
import embedded.koin.core.module.dsl.scopedOf
import embedded.koin.core.qualifier.named
import embedded.koin.dsl.koinApplication
import embedded.koin.dsl.module
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MyAdapter()
class MyViewModel(val adapter: MyAdapter) : ViewModel()
class ScopedComponent()
class MyScope

class DSLExtendedTest {

    @Test
    fun `android dsl`(){
        val m = module {
            factoryOf(::MyAdapter)
            viewModelOf(::MyViewModel)
            viewModelOf(::MyViewModel){
                named("bis")
            }
            scope<MyScope> {
                scopedOf(::ScopedComponent)
            }
        }
        val koin = koinApplication {
            modules(m)
        }.koin
        assertTrue(koin.getOrNull<MyViewModel>() != koin.getOrNull<MyViewModel>(named("bis")))
        assertNotNull(koin.getOrNull<MyViewModel>())
        assertNotNull(koin.getOrNull<MyViewModel>()?.adapter)
        val scope = koin.createScope<MyScope>()
        assertNotNull(scope.getOrNull<ScopedComponent>())
    }

}