package embedded.koin

import embedded.koin.core.context.stopKoin
import kotlin.test.AfterTest

abstract class KoinCoreTest {

    @AfterTest
    fun after() {
        stopKoin()
    }
}
