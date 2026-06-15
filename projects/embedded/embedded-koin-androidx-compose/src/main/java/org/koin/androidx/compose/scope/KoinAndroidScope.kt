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
package embedded.koin.androidx.compose.scope

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import embedded.koin.android.scope.AndroidScopeComponent
import embedded.koin.compose.ComposeContextWrapper
import embedded.koin.compose.LocalKoinScope
import embedded.koin.core.annotation.KoinInternalApi


@OptIn(KoinInternalApi::class)
@Composable
fun KoinActivityScope(
    content: @Composable () -> Unit
) {
    val scope = (LocalContext.current as? AndroidScopeComponent)?.scope
        ?: error("Current context ${LocalContext.current} must implement AndroidScopeComponent interface.")
    CompositionLocalProvider(
        LocalKoinScope provides ComposeContextWrapper(scope),
    ) {
        content()
    }
}

@OptIn(KoinInternalApi::class)
@Composable
fun KoinFragmentScope(
    content: @Composable () -> Unit
) {
    val scope = (LocalContext.current as? AndroidScopeComponent)?.scope
        ?: error("Current context ${LocalContext.current} must implement AndroidScopeComponent interface.")
    CompositionLocalProvider(
        LocalKoinScope provides ComposeContextWrapper(scope),
    ) {
        content()
    }
}