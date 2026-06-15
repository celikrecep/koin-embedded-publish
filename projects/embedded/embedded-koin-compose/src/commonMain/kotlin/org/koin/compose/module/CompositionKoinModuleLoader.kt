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
package embedded.koin.compose.module

import androidx.compose.runtime.RememberObserver
import embedded.koin.core.Koin
import embedded.koin.core.annotation.KoinExperimentalAPI
import embedded.koin.core.annotation.KoinInternalApi
import embedded.koin.core.module.Module

@KoinExperimentalAPI
@KoinInternalApi
class CompositionKoinModuleLoader(
    val modules: List<Module>,
    val koin: Koin,
    val unloadOnForgotten : Boolean,
    val unloadOnAbandoned : Boolean,
) : RememberObserver {

    init {
        koin.logger.debug("$this -> load modules")
        koin.loadModules(modules)
    }

    override fun onRemembered() {
        // Nothing to do
    }

    override fun onForgotten() {
        if (unloadOnForgotten){
            unloadModules()
        }
    }

    override fun onAbandoned() {
        if (unloadOnAbandoned){
            unloadModules()
        }
    }

    private fun unloadModules() {
        koin.logger.debug("$this -> unload modules")
        koin.unloadModules(modules)
    }
}