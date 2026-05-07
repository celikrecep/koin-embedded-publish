//package embedded.koin.core.instance
//
//import org.junit.After
//import org.junit.Assert
//import org.junit.Test
//import embedded.koin.core.context.startKoin
//import embedded.koin.core.context.stopKoin
//import embedded.koin.core.logger.Level
//import embedded.koin.dsl.bind
//import embedded.koin.dsl.factory
//import embedded.koin.dsl.module
//import embedded.koin.dsl.single
//
//class ScopedMVPArchitectureTest {
//
//    val MVPModule = module {
//        single<Repository>()
//        single<View>()
//        factory<Presenter>()
//    }
//
//    val DataSourceModule = module {
//        single<DebugDatasource>() bind Datasource::class
//    }
//
//    @After
//    fun after() {
//        stopKoin()
//    }
//
//    @Test
//    fun `should create all MVP hierarchy`() {
//        stopKoin()
//        val koin = startKoin {
//            printLogger(Level.DEBUG)
//            modules(MVPModule + DataSourceModule)
//        }.koin
//
//        val view = koin.get<View>()
//        val presenter = koin.get<Presenter>()
//        val repository = koin.get<Repository>()
//        val datasource = koin.get<Datasource>()
//
//        Assert.assertNotEquals(presenter, view.presenter)
//        Assert.assertEquals(repository, presenter.repository)
//        Assert.assertEquals(repository, view.presenter.repository)
//        Assert.assertEquals(datasource, repository.datasource)
//    }
//}
