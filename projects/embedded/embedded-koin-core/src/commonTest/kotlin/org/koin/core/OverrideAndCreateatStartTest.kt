package embedded.koin.core

import embedded.koin.KoinCoreTest
import embedded.koin.core.annotation.KoinInternalApi
import embedded.koin.core.context.startKoin
import embedded.koin.core.context.stopKoin
import embedded.koin.core.definition.IndexKey
import embedded.koin.core.instance.InstanceFactory
import embedded.koin.core.logger.Level
import embedded.koin.dsl.koinApplication
import embedded.koin.dsl.module
import embedded.koin.mp.KoinPlatform
import embedded.koin.mp.KoinPlatform.getKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

interface SomeClassInterface
var created = ""
var count = 0

class SomeClassA : SomeClassInterface {
    init {
        println("SomeClassA created")
        count++
        created = "SomeClassA"
    }
}
class SomeClassB : SomeClassInterface {
    init {
        println("SomeClassB created")
        count++
        created = "SomeClassB"
    }
}
class SomeClassC : SomeClassInterface {
    init {
        println("SomeClassC created")
        count++
        created = "SomeClassC"
    }
}


class OverrideAndCreateatStartTest : KoinCoreTest(){
    val moduleA = module {
        single<SomeClassInterface>(createdAtStart = true) {
            SomeClassA()
        }
    }

    val moduleB = module {
        single<SomeClassInterface>(createdAtStart = true) {
            SomeClassB()
        }
    }

    val moduleC = module {
        single<SomeClassInterface> {
            SomeClassC()
        }
    }

    @BeforeTest
    fun setup(){
        count = 0
        created = ""
    }

    @OptIn(KoinInternalApi::class)
    @Test
    fun testDefinitionOverride() {
        startKoin {
            printLogger(Level.DEBUG)
            modules(moduleA + moduleB)
        }

        assertTrue(count == 1)
        assertTrue(created == "SomeClassB")

        getKoin().instanceRegistry.instances.firstNotNullOf { (k: IndexKey,v: InstanceFactory<*>) ->
            assertEquals(k, moduleB.mappings.keys.first())
            assertEquals(v, moduleB.mappings.values.first())
        }
    }

    @OptIn(KoinInternalApi::class)
    @Test
    fun testDefinitionOverride_no_created_at_start() {
        startKoin {
            printLogger(Level.DEBUG)
            modules(moduleA + moduleC)
        }

        assertTrue(count == 0)
        val result = getKoin().get<SomeClassInterface>()
        println("=> $result")
        assertTrue(count == 1)
        assertTrue(created == "SomeClassC")

        getKoin().instanceRegistry.instances.firstNotNullOf { (k: IndexKey,v: InstanceFactory<*>) ->
            assertEquals(k, moduleC.mappings.keys.first())
            assertEquals(v, moduleC.mappings.values.first())
        }
    }
}
