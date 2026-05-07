package embedded.koin.test.android.scope

import embedded.koin.androidx.scope.ActivityScopeArchetype
import embedded.koin.androidx.scope.FragmentScopeArchetype
import embedded.koin.androidx.scope.ActivityRetainedScopeArchetype
import embedded.koin.androidx.scope.dsl.activityScope
import embedded.koin.androidx.scope.dsl.fragmentScope
import embedded.koin.androidx.scope.dsl.activityRetainedScope
import embedded.koin.core.annotation.KoinInternalApi
import embedded.koin.dsl.module
import kotlin.test.Test

@OptIn(KoinInternalApi::class)
class ScopeArchetypeDSLTest {

    class MyScopedClass
    class MyFactoryClass(val ms : MyScopedClass)

    @Test
    fun testArchetypeDSL_activity(){
        val module = module {
            activityScope {
                scoped { MyScopedClass() }
                factory { MyFactoryClass(get()) }
            }
        }

        assert(module.mappings.values.all {
            it.beanDefinition.scopeQualifier == ActivityScopeArchetype
        })
    }

    @Test
    fun testArchetypeDSL_activity_retained(){
        val module = module {
            activityRetainedScope {
                scoped { MyScopedClass() }
                factory { MyFactoryClass(get()) }
            }
        }

        assert(module.mappings.values.all {
            it.beanDefinition.scopeQualifier == ActivityRetainedScopeArchetype
        })
    }

    @Test
    fun testArchetypeDSL_fragment(){
        val module = module {
            fragmentScope {
                scoped { MyScopedClass() }
                factory { MyFactoryClass(get()) }
            }
        }

        assert(module.mappings.values.all {
            it.beanDefinition.scopeQualifier == FragmentScopeArchetype
        })
    }

}