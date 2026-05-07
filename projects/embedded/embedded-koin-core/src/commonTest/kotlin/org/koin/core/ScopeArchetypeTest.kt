package embedded.koin.core

import embedded.koin.core.annotation.KoinInternalApi
import embedded.koin.core.component.getScopeId
import embedded.koin.core.logger.Level
import embedded.koin.core.module.KoinDslMarker
import embedded.koin.core.module.Module
import embedded.koin.core.qualifier.TypeQualifier
import embedded.koin.dsl.ScopeDSL
import embedded.koin.dsl.koinApplication
import embedded.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ScopeArchetypeTest {

    open class Archetype
    class ArchetypeExt : Archetype()
    class ClassA

    @KoinDslMarker
    fun Module.scopeArchetype(scopeSet: ScopeDSL.() -> Unit) {
        val qualifier = TypeQualifier(Archetype::class)
        ScopeDSL(qualifier, this).apply(scopeSet)
    }

    @OptIn(KoinInternalApi::class)
    @Test
    fun moduleArchetypeData() {
        val scopeModule = module {
            scopeArchetype {
                scoped { ClassA() }
            }
        }
        assertEquals(
            scopeModule.mappings.values.first().beanDefinition.scopeQualifier, TypeQualifier(Archetype::class)
        )
    }

    @Test
    fun declareAndRunArchetype() {
        val scopeModule = module {
            scopeArchetype {
                scoped { ClassA() }
            }
        }
        val koin = koinApplication {
            printLogger(Level.DEBUG)
            modules(scopeModule)
        }.koin

        val archetypeExt = ArchetypeExt()
        val scope = koin.createScope<ArchetypeExt>(
            archetypeExt.getScopeId(), archetypeExt,
            TypeQualifier(Archetype::class)
        )

        val a = scope.getOrNull<ClassA>()
        assertNotNull(a)
    }

    @Test
    fun declareAndRunArchetypeWithoutSource() {
        val scopeModule = module {
            scopeArchetype {
                scoped { ClassA() }
            }
        }
        val koin = koinApplication {
            printLogger(Level.DEBUG)
            modules(scopeModule)
        }.koin

        val archetypeExt = ArchetypeExt()
        val scope = koin.createScope<Archetype>(archetypeExt.getScopeId(), scopeArchetype = TypeQualifier(Archetype::class))

        val a = scope.getOrNull<ClassA>()
        assertNotNull(a)
    }
}
