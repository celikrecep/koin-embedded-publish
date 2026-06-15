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
@file:OptIn(KoinInternalApi::class)

package embedded.koin.compose.scope

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import embedded.koin.compose.getKoin
import embedded.koin.core.Koin
import embedded.koin.core.annotation.KoinExperimentalAPI
import embedded.koin.core.annotation.KoinInternalApi
import embedded.koin.core.qualifier.Qualifier
import embedded.koin.core.qualifier.StringQualifier
import embedded.koin.core.scope.Scope
import embedded.koin.core.scope.ScopeID

/**
 * Remember Koin Scope & run CompositionKoinScopeLoader to handle scope closure
 *
 * @param scope - Koin scope
 * @author Arnaud Giuliani
 */
@KoinExperimentalAPI
@Composable
fun rememberKoinScope(scope: Scope): Scope {
    val wrapper = remember(scope) {
        CompositionKoinScopeLoader(scope)
    }
    return wrapper.scope
}