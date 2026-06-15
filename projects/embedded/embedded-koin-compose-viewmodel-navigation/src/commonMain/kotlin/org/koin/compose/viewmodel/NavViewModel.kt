/*
 * Copyright 2017-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package embedded.koin.compose.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import embedded.koin.compose.currentKoinScope
import embedded.koin.core.annotation.KoinExperimentalAPI
import embedded.koin.core.annotation.KoinInternalApi
import embedded.koin.core.parameter.ParametersDefinition
import embedded.koin.core.qualifier.Qualifier
import embedded.koin.core.scope.Scope
import embedded.koin.viewmodel.defaultExtras
import embedded.koin.viewmodel.resolveViewModel

/*
    Ported directly from Android side. Waiting more feedback
 */

/**
 * Resolve ViewModel instance with Navigation NavBackStackEntry as extras parameters
 *
 * @param qualifier
 * @param parameters
 *
 * @author Arnaud Giuliani
 */
@OptIn(KoinInternalApi::class)
@Deprecated(
    message = "koinNavViewModel is deprecated. Use koinViewModel instead.",
    replaceWith = ReplaceWith(
        expression = "koinViewModel()",
        imports = ["embedded.koin.compose.viewmodel.koinViewModel"]
    )
)
@Composable
inline fun <reified T : ViewModel> koinNavViewModel(
    qualifier: Qualifier? = null,
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    key: String? = null,
    extras: CreationExtras = defaultExtras(viewModelStoreOwner),
    scope: Scope = currentKoinScope(),
    noinline parameters: ParametersDefinition? = null,
): T {
    return resolveViewModel(
        T::class, viewModelStoreOwner.viewModelStore, key, extras, qualifier, scope, parameters
    )
}

/**
 * Reuse ViewModel instance from NavBackStackEntry if any, else create ViewModel instance
 * It finds the parentEntry with "navController.getBackStackEntry(navGraphRoute)"
 *
 * Originally from Philipp Lackner - CMP Bookpedia app
 *
 * @author Arnaud Giuliani
 */
 @Composable
inline fun <reified VM : ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController,
    navGraphRoute: Any? = this.destination.parent?.route,
): VM {
    val navGraphRoute = navGraphRoute ?: return koinViewModel<VM>()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(
        viewModelStoreOwner = parentEntry
    )
}
