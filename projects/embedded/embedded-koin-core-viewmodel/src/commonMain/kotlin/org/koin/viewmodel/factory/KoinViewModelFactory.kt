/*
 * Copyright 2017-Present the original author or authors.
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
package embedded.koin.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import embedded.koin.core.annotation.KoinInternalApi
import embedded.koin.core.option.hasViewModelScopeFactory
import embedded.koin.core.parameter.ParametersDefinition
import embedded.koin.core.qualifier.Qualifier
import embedded.koin.core.qualifier.TypeQualifier
import embedded.koin.core.scope.Scope
import embedded.koin.core.scope.ScopeID
import embedded.koin.mp.KoinPlatformTools
import embedded.koin.mp.generateId
import embedded.koin.viewmodel.scope.ViewModelScopeArchetype
import kotlin.reflect.KClass

/**
 * ViewModelProvider.Factory for Koin instances resolution
 * @see ViewModelProvider.Factory
 */
class KoinViewModelFactory(
    private val kClass: KClass<out ViewModel>,
    private val scope: Scope,
    private val qualifier: Qualifier? = null,
    private val params: ParametersDefinition? = null
) : ViewModelProvider.Factory {

    @OptIn(KoinInternalApi::class)
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        val androidParams = AndroidParametersHolder(params, extras)
        val koin = scope.getKoin()
        return if (!koin.optionRegistry.hasViewModelScopeFactory()){
            scope.getWithParameters(kClass, qualifier, androidParams)
        } else {
            val scopeId = getViewModelScopeId(modelClass)
            val vmScope = koin.createScope(scopeId, TypeQualifier(modelClass), null, ViewModelScopeArchetype)
            val vm : T = vmScope.getWithParameters(kClass, qualifier, androidParams)
            vm.addCloseable(ViewModelScopeAutoCloseable(scopeId,koin))
            vm
        }
    }

    @KoinInternalApi
    private fun <T : ViewModel> getViewModelScopeId(modelClass: KClass<T>) : ScopeID = "${modelClass.simpleName}-${KoinPlatformTools.generateId()}"
}

