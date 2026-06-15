package embedded.koin.compose

import android.app.Application
import android.content.ComponentCallbacks
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import embedded.koin.dsl.KoinConfiguration
import androidx.compose.ui.platform.LocalContext
import embedded.koin.android.ext.android.getKoin
import embedded.koin.android.ext.koin.androidContext
import embedded.koin.android.ext.koin.androidLogger
import embedded.koin.core.Koin
import embedded.koin.core.component.KoinComponent
import embedded.koin.core.logger.Level
import embedded.koin.dsl.koinConfiguration
import embedded.koin.dsl.includes

@Composable
internal actual fun composeMultiplatformConfiguration(loggerLevel : Level, config : KoinConfiguration) : KoinConfiguration {
    val appContext = LocalContext.current.applicationContext ?: error("Android ApplicationContext not found in current Compose context!")
    return koinConfiguration {
        androidContext(appContext)
        androidLogger(loggerLevel)
        includes(config)
    }
}

@Deprecated("KoinContext is not needed anymore. This can be removed. Compose Koin context is setup with StartKoin()")
@Composable
internal actual fun retrieveDefaultInstance() : Koin {
    val context = LocalContext.current
    val koin = remember(context) {
        context.findContextForKoin().getKoin()
    }
    return koin
}

@Deprecated("KoinContext is not needed anymore. This can be removed. Compose Koin context is setup with StartKoin()")
private fun Context.findContextForKoin(): ComponentCallbacks {
    var context = this
    while (context is ContextWrapper) {
        if (context is KoinComponent && context is ComponentCallbacks) return context
        context = context.baseContext
    }
    return applicationContext as Application
}