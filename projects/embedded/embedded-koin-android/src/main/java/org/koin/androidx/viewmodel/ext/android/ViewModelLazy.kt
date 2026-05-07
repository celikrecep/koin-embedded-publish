@file:Suppress("DEPRECATION")

package embedded.koin.androidx.viewmodel.ext.android

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import embedded.koin.android.ext.android.getKoinScope
import embedded.koin.core.annotation.KoinInternalApi
import embedded.koin.core.parameter.ParametersDefinition
import embedded.koin.core.qualifier.Qualifier
import embedded.koin.viewmodel.SavedStateDefinition
import embedded.koin.viewmodel.resolveViewModel
import embedded.koin.viewmodel.toExtras
import kotlin.reflect.KClass

@OptIn(KoinInternalApi::class)
@MainThread
fun <T : ViewModel> ComponentActivity.viewModelForClass(
    clazz: KClass<T>,
    qualifier: Qualifier? = null,
    owner: ViewModelStoreOwner = this,
    state: SavedStateDefinition? = null,
    key: String? = null,
    parameters: ParametersDefinition? = null,
): Lazy<T> {
    val viewModelStore = owner.viewModelStore
    return lazy(LazyThreadSafetyMode.NONE) {
        resolveViewModel(
            clazz,
            viewModelStore,
            extras = state?.invoke()?.toExtras(owner) ?: CreationExtras.Empty,
            qualifier = qualifier,
            parameters = parameters,
            key = key,
            scope = getKoinScope()
        )
    }
}

@OptIn(KoinInternalApi::class)
@MainThread
fun <T : ViewModel> Fragment.viewModelForClass(
    clazz: KClass<T>,
    qualifier: Qualifier? = null,
    owner: () -> ViewModelStoreOwner = { this },
    state: SavedStateDefinition? = null,
    key: String? = null,
    parameters: ParametersDefinition? = null,
): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        val ownerEager = owner()
        val viewModelStore = ownerEager.viewModelStore
        resolveViewModel(
            clazz,
            viewModelStore,
            extras = state?.invoke()?.toExtras(ownerEager) ?: CreationExtras.Empty,
            qualifier = qualifier,
            parameters = parameters,
            key = key,
            scope = getKoinScope()
        )
    }
}