package ru.polescanner.validation_etude.ui.signin

import androidx.compose.runtime.Immutable
import arrow.core.Either
import arrow.core.raise.either
import ru.polescanner.validation_etude.domain.general.Login
import ru.polescanner.validation_etude.domain.general.Password
import ru.polescanner.validation_etude.domain.general.check
import ru.polescanner.validation_etude.domain.security.Credentials
import ru.polescanner.validation_etude.ui.reusable.util.UiEvent
import ru.polescanner.validation_etude.ui.reusable.util.UiState
import ru.polescanner.validation_etude.ui.reusable.util.UiText
import ru.polescanner.validation_etude.ui.reusable.util.ValidOrFocusedAtCheck
import ru.polescanner.validation_etude.ui.reusable.util.atMostOneFocused
import ru.polescanner.validation_etude.ui.reusable.util.parseOrPrompt
import ru.polescanner.validation_etude.ui.reusable.util.withClearedFocus

@Immutable
sealed interface SignInState: UiState {
    data object Loading: SignInState
    data class Main(
        val login: ValidOrFocusedAtCheck<String> = "".withClearedFocus(),
        val password: ValidOrFocusedAtCheck<String> = "".withClearedFocus(),
        val rememberMe: ValidOrFocusedAtCheck<Boolean> = false.withClearedFocus(),
    ): SignInState {
        init {
            require( atMostOneFocused() ) { "only one view can be focused!" }
        }

        fun toCredentials(inform: (UiText) -> Unit): Either<UiState, Credentials> = either {
            Credentials(
                ::login.parseOrPrompt(inform) { Login(it) }.bind(),
                ::password.parseOrPrompt(inform) { Password(it) }.bind(),
                ::rememberMe.parseOrPrompt(inform) { it.check() }.bind()
            )
        }
    }
    // ToDo Do we need that class?
    data class Error(val error: UiText): SignInState
}

@Immutable
sealed interface SignInEvent: UiEvent {
    //MainScreen editing
    data class OnLoginChanged(val login: String): SignInEvent
    data class OnPasswordChanged(val password: String): SignInEvent
    data class OnRememberMeFor30DaysChanged(val remember: Boolean): SignInEvent

    //MainScreen actions clicks
    data object OnSubmit: SignInEvent
}