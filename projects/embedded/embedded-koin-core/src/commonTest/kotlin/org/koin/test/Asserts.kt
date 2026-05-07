package embedded.koin.test

import embedded.koin.mp.KoinPlatformTools
import kotlin.test.assertNull

fun assertHasNoStandaloneInstance() {
    assertNull(KoinPlatformTools.defaultContext().getOrNull())
}
