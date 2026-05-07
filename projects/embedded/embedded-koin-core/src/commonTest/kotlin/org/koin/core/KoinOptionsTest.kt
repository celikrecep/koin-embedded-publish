package embedded.koin.core

import embedded.koin.core.annotation.KoinExperimentalAPI
import embedded.koin.core.annotation.KoinInternalApi
import embedded.koin.core.option.KoinOption
import embedded.koin.core.option.hasViewModelScopeFactory
import embedded.koin.core.option.viewModelScopeFactory
import embedded.koin.dsl.koinApplication
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(KoinExperimentalAPI::class, KoinInternalApi::class)
class KoinOptionsTest {

    @Test
    fun has_no_koin_flag(){

        val koin = koinApplication {
        }.koin

        assertNull(koin.optionRegistry.getOrNull<Boolean>(KoinOption.VIEWMODEL_SCOPE_FACTORY))
        assertFalse(koin.optionRegistry.hasViewModelScopeFactory())
    }

    @Test
    fun has_no_koin_flag_empty(){

        val koin = koinApplication {
            options()
        }.koin

        assertNull(koin.optionRegistry.getOrNull<Boolean>(KoinOption.VIEWMODEL_SCOPE_FACTORY))
        assertFalse(koin.optionRegistry.hasViewModelScopeFactory())
    }

    @Test
    fun can_set_koin_flag(){

        val koin = koinApplication {
            options(
                viewModelScopeFactory()
            )
        }.koin

        assertTrue(koin.optionRegistry.getOrNull<Boolean>(KoinOption.VIEWMODEL_SCOPE_FACTORY)!!)
        assertTrue(koin.optionRegistry.hasViewModelScopeFactory())
    }
}