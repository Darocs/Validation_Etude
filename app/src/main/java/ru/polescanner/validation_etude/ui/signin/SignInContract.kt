package ru.polescanner.validation_etude.ui.signin

import androidx.compose.runtime.Immutable
import arrow.core.Either
import arrow.core.zip
import ru.polescanner.validation_etude.R
import ru.polescanner.validation_etude.domain.general.Login
import ru.polescanner.validation_etude.domain.general.Name
import ru.polescanner.validation_etude.domain.general.Password
import ru.polescanner.validation_etude.domain.security.Credentials
import ru.polescanner.validation_etude.ui.reusable.kotlinapi.toStateWithFocus
import ru.polescanner.validation_etude.ui.reusable.util.UiEvent
import ru.polescanner.validation_etude.ui.reusable.util.UiState
import ru.polescanner.validation_etude.ui.reusable.util.UiText
import ru.polescanner.validation_etude.ui.reusable.util.ValidOrFocusedAtCheck
import ru.polescanner.validation_etude.ui.reusable.util.atMostOneFocused
import ru.polescanner.validation_etude.ui.reusable.util.toFocusable
import kotlin.reflect.KProperty0

@Immutable
sealed interface SignInState: UiState {
    data object Loading: SignInState
    data class Main(
        val login: ValidOrFocusedAtCheck<String> = "".toFocusable(),
        val password: ValidOrFocusedAtCheck<String> = "".toFocusable(),
        val isLoggedIn: Boolean = false
    ): SignInState { //ToDo keepMeLoggedIn isTokenExpired RememberMe etc. - choose the best

        init {
            require(
                atMostOneFocused(login, password)
            )
        }

        val isValid: Boolean = listOf(
            login.value.isNotBlank(),
            password.value.isNotBlank()
        ).all { it }

        fun checkIfValid(informUser: (Int) -> Unit): Main =
            when {
                login.value.isBlank() -> {
                    informUser(R.string.login_error)
                    copy(login = login.setFocus())
                }

                password.value.isBlank() -> {
                    informUser(R.string.password_error)
                    copy(password = password.setFocus())
                }

                else -> this
            }

        fun clearFocus(): Main = this.copy(
            login = login.clearFocus(),
            password = password.clearFocus()
        )

        fun toCredentialsArr(inform: (UiText) -> Unit) : Either<Main, Credentials> {
            fun <P> toState(
                e: Name.ValidationError,
                focusedView: KProperty0<ValidOrFocusedAtCheck<P>>
            ) : Main = toStateWithFocus(this, inform, e, focusedView) as Main

            return login.toLogin().mapLeft { toState(it, ::login) }.zip(
                password.value.toPassword().mapLeft { toState(it, ::password) },
                password.value.toPassword().mapLeft { toState(it, ::password) }
            ) { a, b, c ->
                    Credentials(a, b, false)
            }
        }

    }
    data class Error(val error: UiText): SignInState
    data class ForgotPassword(val email: String = ""): SignInState
    data object License: SignInState
}

private fun String.toLogin() = Login(this)
private fun ValidOrFocusedAtCheck<String>.toLogin() = Login(this.value)
private fun String.toPassword() = Password(this)

@Immutable
sealed interface SignInEvent: UiEvent {
    //MainScreen editing
    data class OnLoginChanged(val login: String): SignInEvent
    data class OnPasswordChanged(val password: String): SignInEvent
    data class OnRememberMeFor30DaysChanged(val remember: Boolean): SignInEvent
    //MainScreen actions clicks
    data class OnLogin(
        val username: String,
        val password: String,
        val rememberMeFor30Days: Boolean
    ): SignInEvent
    data object OnSignUp: SignInEvent
    data object OnLicense: SignInEvent
    data object OnForgotPassword: SignInEvent

    //ForGotPassword
    data class OnEmailChanged(val email: String): SignInEvent
    data class OnSubmitEmail(val email: String): SignInEvent

    //LicenseScreen
    data object OnLicenseAccept: SignInEvent

    //Final transit to PoleDroid
    data object OnSuccess: SignInEvent
}