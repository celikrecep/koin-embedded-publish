package embedded.koin.androidx.scope

import androidx.lifecycle.ViewModel
import embedded.koin.core.annotation.KoinExperimentalAPI
import embedded.koin.core.component.KoinScopeComponent
import embedded.koin.core.scope.Scope
import embedded.koin.viewmodel.scope.viewModelScope

/**
 * Class to help support Koin Scope in a ViewModel
 * create directly a scope instance for current ViewModel
 *
 * allow to intercept before scope closing with `onCloseScope`, to be overriden
 *
 * Destroy linked scope with `onCleared`
 *
 * @author Arnaud Giuliani
 */
@OptIn(KoinExperimentalAPI::class)
@Deprecated("ScopeViewModel has been moved to embedded.koin.viewmodel.scope.ScopeViewModel (koin-core-viewmodel)", ReplaceWith(expression = "ScopeViewModel()", imports = ["embedded.koin.viewmodel.scope"]))
abstract class ScopeViewModel : ViewModel(), KoinScopeComponent {

    override val scope: Scope = viewModelScope()

    /**
     * To override to add behavior before closing Scope
     */
    open fun onCloseScope(){}

    override fun onCleared() {
        onCloseScope()
        scope.close()
        super.onCleared()
    }
}