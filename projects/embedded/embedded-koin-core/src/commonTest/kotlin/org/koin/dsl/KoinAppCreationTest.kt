package embedded.koin.dsl

import embedded.koin.KoinCoreTest
import embedded.koin.Simple.ComponentA
import embedded.koin.Simple.ComponentB
import embedded.koin.core.annotation.KoinInternalApi
import embedded.koin.core.context.startKoin
import embedded.koin.core.context.stopKoin
import embedded.koin.core.error.KoinApplicationAlreadyStartedException
import embedded.koin.core.logger.Level
import embedded.koin.core.module.dsl.singleOf
import embedded.koin.mp.KoinPlatform
import embedded.koin.mp.KoinPlatformTools
import embedded.koin.test.assertDefinitionsCount
import embedded.koin.test.assertHasNoStandaloneInstance
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.fail

@OptIn(KoinInternalApi::class)
class KoinAppCreationTest : KoinCoreTest() {

    @Test
    fun `make a Koin application`() {
        val app = koinApplication { }

        app.assertDefinitionsCount(0)

        assertHasNoStandaloneInstance()
    }

    @Test
    fun `maintain koinApplication source compatibility`() {
        // Successful compilation and a lack of exceptions is sufficient
        koinApplication { modules() }
        koinApplication(createEagerInstances = true)
        koinApplication(createEagerInstances = true) { modules() }
        koinApplication(true) { modules() }
        val declaration: KoinAppDeclaration = { modules() }
        koinApplication(declaration)
        koinApplication(createEagerInstances = true, declaration)
        koinApplication(createEagerInstances = true, appDeclaration = declaration)
        koinApplication(true, declaration)
    }

    @Test
    fun `start a Koin application`() {
        val app = startKoin { }

        assertEquals(KoinPlatformTools.defaultContext().get(), app.koin)

        stopKoin()

        assertHasNoStandaloneInstance()
    }

    @Test
    fun `can't restart a Koin application`() {
        startKoin { }
        try {
            startKoin { }
            fail("should throw  KoinAppAlreadyStartedException")
        } catch (e: KoinApplicationAlreadyStartedException) {
            e.printStackTrace()
        }
    }

    @Test
    fun `allow declare a logger`() {
        startKoin {
            logger(KoinPlatformTools.defaultLogger(Level.ERROR))
        }

        assertEquals(KoinPlatformTools.defaultContext().get().logger.level, Level.ERROR)

        KoinPlatformTools.defaultContext().get().logger.debug("debug")
        KoinPlatformTools.defaultContext().get().logger.info("info")
        KoinPlatformTools.defaultContext().get().logger.error("error")
    }

    @Test
    fun `allow declare a configuration`() {
        val config = koinConfiguration {
            printLogger(Level.DEBUG)
        }

        koinApplication(config)
        startKoin(config)
    }

    @Test
    fun `allow declare a configuration extension`() {
        val config1 = koinConfiguration {
            printLogger(Level.DEBUG)
            modules(module {
                singleOf(::ComponentA)
            })
        }

        val config2 = koinConfiguration {
            includes(config1)

            modules(module {
                singleOf(::ComponentB)
            })
        }

        val k = koinApplication(config2).koin

        assertEquals(
            2,
            k.instanceRegistry.instances.size
        )
        assertNotNull(k.getOrNull<ComponentB>())
    }

    fun init(config : KoinAppDeclaration? = null){
        startKoin {
            printLogger(Level.DEBUG)
            includes(config)
            modules(module {
                singleOf(::ComponentA)
            })
        }
    }

    @Test
    fun `allow declare a configuration extension - from function`() {
        init()
        assertNotNull(KoinPlatform.getKoin().getOrNull<ComponentA>())
        stopKoin()

        init {
            modules(
                module {
                    singleOf(::ComponentB)
                }
            )
        }
        assertNotNull(KoinPlatform.getKoin().getOrNull<ComponentB>())
        stopKoin()
    }

//    @Test
//    fun `allow declare a print logger level`() {
//        startKoin {
//            printLogger(Level.ERROR)
//        }
//
//        assertEquals(PlatformTools.defaultContext().get().logger.level, Level.ERROR)
//
//        PlatformTools.defaultContext().get().logger.debug("debug")
//        PlatformTools.defaultContext().get().logger.info("info")
//        PlatformTools.defaultContext().get().logger.error("error")
//    }
}
