package embedded.koin.koincomponent

import embedded.koin.KoinCoreTest
import embedded.koin.core.component.KoinComponent
import embedded.koin.core.component.inject
import embedded.koin.core.context.startKoin
import embedded.koin.core.context.stopKoin
import embedded.koin.core.logger.Level
import embedded.koin.core.qualifier.named
import embedded.koin.dsl.bind
import embedded.koin.dsl.module
import kotlin.test.Test

class TODOAppTest : KoinCoreTest(){

    val todoAppModule = module {
        single { TasksView() } bind TasksContract.View::class
        single { TasksPresenter(get()) } bind TasksContract.Presenter::class
    }

    val repositoryModule = module {
        single(named("remoteDataSource")) { FakeTasksRemoteDataSource() } bind TasksDataSource::class
        single(named("localDataSource")) { TasksLocalDataSource() } bind TasksDataSource::class
        single {
            TasksRepository(
                get(named("remoteDataSource")),
                get(named("localDataSource")),
            )
        } bind TasksDataSource::class
    }

    interface TasksContract {
        interface View
        interface Presenter
    }

    class TasksView : KoinComponent, TasksContract.View {
        val taskPreenter by inject<TasksContract.Presenter>()
    }

    class TasksPresenter(val tasksRepository: TasksRepository) : TasksContract.Presenter
    interface TasksDataSource
    class FakeTasksRemoteDataSource : TasksDataSource
    class TasksLocalDataSource : TasksDataSource
    class TasksRepository(
        val remoteDataSource: TasksDataSource,
        val localDatasource: TasksDataSource,
    ) : TasksDataSource

    @Test
    fun should_create_all_components() {
        val koinApp = startKoin {
            printLogger(Level.DEBUG)
            modules(todoAppModule + repositoryModule)
        }
        val koin = koinApp.koin

        val view = koin.get<TasksView>()
        println("-> ${view.taskPreenter}")
        stopKoin()
    }
}
