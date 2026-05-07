package embedded.koin.core

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import embedded.koin.core.annotation.KoinInternalApi
import embedded.koin.core.component.getScopeId
import embedded.koin.core.context.startKoin
import embedded.koin.core.context.stopKoin
import embedded.koin.dsl.module
import embedded.koin.mp.KoinPlatform
import embedded.koin.mp.KoinPlatformTools
import embedded.koin.mp.Lockable
import embedded.koin.mp.generateId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ConcurrencyTest {
    data class Counter(val id : String = KoinPlatformTools.generateId())
    class CounterService {

        private var counter = 0
        private val lock = Lockable()

        fun increment() = KoinPlatformTools.synchronized(lock) {
            counter++
        }

        fun getCounter(): Int = KoinPlatformTools.synchronized(lock) { counter }
    }

    val jobList = 100
    val jobRepeat = 100
    val scopeList = 10_000

    @Test
    fun parallel_access() = runTest {

        startKoin {
            modules(module {
                single { CounterService() }
            })
        }

        try {
            // Use a coroutine scope for concurrency
            coroutineScope {
                val jobs = List(jobList) {
                    launch {
                        repeat(jobRepeat) {
                            KoinPlatform.getKoin().get<CounterService>().increment()
                        }
                    }
                }
                jobs.joinAll()
            }

            assertEquals(jobList * jobRepeat, KoinPlatform.getKoin().get<CounterService>().getCounter())
        } finally {
            stopKoin()
        }
    }


    @OptIn(KoinInternalApi::class)
    @Test
    fun parallel_create_scopes() = runTest {

        startKoin {
            modules(module {
                scope<Counter>{
                    scoped { CounterService() }
                }
            })
        }

        val result = KoinPlatformTools.safeHashMap<String,Int>()

        try {
            coroutineScope {
                val startScopes = List(scopeList) {
                    launch {
                        KoinPlatform.getKoin().apply {
                            val counter = Counter()
                            val scope = createScope<Counter>(counter.getScopeId())
                            scope.get<CounterService>().increment()
                            result[scope.id] = scope.get<CounterService>().getCounter()
                            scope.close()
                        }
                    }
                }
                startScopes.joinAll()
            }
            assertTrue(KoinPlatform.getKoin().instanceRegistry.instances.values.none { it.isCreated() })
        } finally {
            stopKoin()
        }
        assertEquals(scopeList, result.size)
        assertTrue(result.values.all { it == 1 })
    }

}