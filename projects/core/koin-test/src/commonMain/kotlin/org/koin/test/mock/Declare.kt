package embedded.koin.test.mock

import embedded.koin.core.qualifier.Qualifier
import embedded.koin.mp.KoinPlatformTools
import embedded.koin.test.KoinTest
import embedded.koin.test.get

inline fun <reified T : Any> KoinTest.declare(
    qualifier: Qualifier? = null,
    noinline instance: () -> T,
): T {
    val koin = KoinPlatformTools.defaultContext().get()
    koin.declare(instance(), qualifier, allowOverride = true)
    return get(qualifier)
}
