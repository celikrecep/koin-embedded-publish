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

package embedded.koin.mp

import embedded.koin.core.context.GlobalContext
import embedded.koin.core.context.KoinContext
import embedded.koin.core.instance.InstanceFactory
import embedded.koin.core.logger.Level
import embedded.koin.core.logger.Logger
import embedded.koin.core.logger.PrintLogger
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

actual object KoinPlatformTools {
    actual fun getStackTrace(e: Exception): String = e.toString() + InstanceFactory.ERROR_SEPARATOR + e.stackTrace.takeWhile { !it.className.contains("sun.reflect") }.joinToString(InstanceFactory.ERROR_SEPARATOR)
    actual fun getClassName(kClass: KClass<*>): String = kClass.java.name
    actual fun getClassFullNameOrNull(kClass: KClass<*>): String? = kClass.qualifiedName
    actual fun defaultLazyMode(): LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED
    actual fun defaultLogger(level: Level): Logger = PrintLogger(level)
    actual fun defaultContext(): KoinContext = GlobalContext
    actual fun <R> synchronized(lock: Lockable, block: () -> R) = kotlin.synchronized(lock, block)
    actual fun <K, V> safeHashMap(): MutableMap<K, V> = ConcurrentHashMap<K, V>()
    actual fun <K> safeSet(): MutableSet<K> = Collections.newSetFromMap(ConcurrentHashMap())
}