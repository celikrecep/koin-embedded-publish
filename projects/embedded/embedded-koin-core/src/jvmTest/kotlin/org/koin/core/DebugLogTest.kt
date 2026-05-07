package embedded.koin.core

import org.junit.Test
import embedded.koin.KoinCoreTest
import embedded.koin.core.annotation.KoinInternalApi
import embedded.koin.core.context.startKoin
import embedded.koin.core.context.stopKoin
import embedded.koin.core.logger.Level

@OptIn(KoinInternalApi::class)
class DebugLogTest : KoinCoreTest(){

    @Test
    fun fasterDebug() {
        val koin = startKoin {
            printLogger(Level.INFO)
        }.koin

        for (index in 1..10) {
            measureDuration("with if") {
                if (koin.logger.isAt(Level.DEBUG)) {
                    koin.logger.debug(message { "test me" })
                }
            }
            measureDuration("with fct") {
                koin.logger.log(Level.DEBUG) { "test me" }
            }
        }
        stopKoin()
    }

    fun message(msg: () -> String): String {
        println("resolve message")
        return msg()
    }
}
