package ru.polescanner.validation_etude.ui.signin


import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.polescanner.validation_etude.ui.reusable.util.AbstractViewModel
import ru.polescanner.validation_etude.ui.reusable.util.UiText
import ru.polescanner.validation_etude.ui.reusable.util.clearFocus
import ru.polescanner.validation_etude.ui.reusable.util.withClearedFocus
import ru.polescanner.validation_etude.ui.reusable.util.withClearedFocusForNullable
import ru.polescanner.validation_etude.ui.signin.SignInEvent.OnLoginChanged
import ru.polescanner.validation_etude.ui.signin.SignInEvent.OnPasswordChanged
import ru.polescanner.validation_etude.ui.signin.SignInEvent.OnRememberMeChanged
import ru.polescanner.validation_etude.ui.signin.SignInEvent.OnSubmit

class SignInViewModel : AbstractViewModel<SignInState, SignInEvent>() {
    override val _stateFlow: MutableStateFlow<SignInState> = MutableStateFlow(SignInState.Loading)
    override val stateFlow: StateFlow<SignInState> = _stateFlow.asStateFlow()

    override var state: SignInState
        get() = _stateFlow.value
        set(newState) {
            viewModelScope.launch { _stateFlow.update { newState } }
        }

    init {
        viewModelScope.launch {
            _stateFlow.update { SignInState.Main() }
        }
    }

    private fun state() = state as SignInState.Main // ToDo Consider to move to AbstractVM
    override fun onEvent(e: SignInEvent) {
        when (e) { //MainScreen editing
            is OnLoginChanged -> state = state().clearFocus().copy(login = e.login.withClearedFocus())

            is OnPasswordChanged -> state =
                state().clearFocus().copy(password = e.password.withClearedFocus())

            is OnRememberMeChanged -> state =
                state().clearFocus().copy(rememberMe = e.remember.withClearedFocusForNullable())

            //MainScreen actions click
            // ToDo Check that we don't use e content but use state!!!!
            is OnSubmit -> state()
                .toCredentials { inform(it) }
                .fold(
                    ifLeft = { state = it as SignInState.Main },
                    ifRight = { _snackbarText.value =
                        UiText.Str("Success sing in: login:${it.login.value}, password:${it.password.value}, rememberMe:${it.rememberMe}")
                    }
                )
        }
    }
}
