package ru.polescanner.validation_etude.ui.signin


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import arrow.core.Either
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.polescanner.core.general.User
import ru.polescanner.droidmvp.datasource.dto.AuthRequestDTO
import ru.polescanner.droidmvp.datasource.dto.PoleServerApi
import ru.polescanner.droidmvp.datasource.dto.UserDTO
import ru.polescanner.droidmvp.datasource.dto.ktorHttpClient
import ru.polescanner.droidmvp.datasource.dto.ktorHttpClientAuth
import ru.polescanner.droidmvp.datasource.dto.toEntity
import ru.polescanner.droidmvp.domainext.general.TrialMode
import ru.polescanner.droidmvp.ui.reusable.util.toFocusable
import ru.polescanner.validation_etude.ui.reusable.util.AbstractViewModel
import ru.polescanner.validation_etude.ui.reusable.util.UiText
import ru.polescanner.validation_etude.ui.reusable.util.toFocusable
import java.time.Instant
import java.time.temporal.ChronoUnit

private const val TAG = "Welcome SignIn"
class SignInViewModel(
    var userId: String?,
    var isLoggedIn: Boolean = false,
    val onSuccess: (User) -> Unit = {}
): AbstractViewModel<SignInState, SignInEvent>() {
    class Factory(
        val navigator: DestinationsNavigator,
        val userId: String? = null,
        val isLoggedIn: Boolean = false,
        val onSuccess: (User) -> Unit
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T = SignInViewModel(
            navigator = navigator,
            userId = userId,
            isLoggedIn = isLoggedIn,
            onSuccess = onSuccess
        ) as T
    }

    override val _stateFlow: MutableStateFlow<SignInState> = MutableStateFlow(SignInState.Loading)
    override val stateFlow: StateFlow<SignInState> = _stateFlow.asStateFlow()

    override var state: SignInState
        get() = _stateFlow.value
        set(newState) {
            viewModelScope.launch { _stateFlow.update { newState } }
        }

    init {
        viewModelScope.launch {
            if (isLoggedIn) {
                prefsRepo.userTokens()?.let{
                    onEvent(SignInEvent.OnSuccess)
                }
                    ?: _stateFlow.update { SignInState.Main() }
            } else {
                _stateFlow.update { SignInState.Main() }
            }
        }
    }

    override fun onEvent(e: SignInEvent) {
        when (e) { //MainScreen editing
            is SignInEvent.OnLoginChanged -> state =
                (state as SignInState.Main).clearFocus().copy(login = e.login.toFocusable())

            is SignInEvent.OnPasswordChanged -> state =
                (state as SignInState.Main).clearFocus().copy(password = e.password.toFocusable())

            is SignInEvent.OnRememberMeFor30DaysChanged -> state =
                (state as SignInState.Main).copy(isLoggedIn = e.remember)

            //MainScreen actions click
            is SignInEvent.OnLogin -> {
                val result = (state as SignInState.Main).toCredentialsArr {
                    _snackbarText.value = it
                }
                if (result is Either.Left) state = result.value
                else toServer((result as Either.Right).value)
            }
            /*               review(
                           e.username,
                           e.password,
                           e.rememberMeFor30Days
                       )*/

            SignInEvent.OnLicense -> state = SignInState.License

            SignInEvent.OnForgotPassword -> state = SignInState.ForgotPassword()

            //License
            SignInEvent.OnLicenseAccept -> state =
                SignInState.Main() //ToDo with license we loose all data like login...
            //ToDo maybe we have to remember date and time when License was accepted?

            //Final transit to PoleDroid
            is SignInEvent.OnSuccess -> {
                viewModelScope.launch { //ToDo Костыль!!! TrialMode
                    val userId = prefsRepo.userTokens()!!.userId
                    if (userId == TrialMode.id()) {
                        onSuccess(TrialMode)
                    } else {
                        userRepo.count(userId).collect {
                            val user = if (it == 0) {
                                val newUser = PoleServerApi(ktorHttpClientAuth(prefsRepo = prefsRepo))
                                    .client.get("users/myid/${userId}")
                                    .body<UserDTO>().toEntity()
                                userRepo.add(newUser)
                                newUser
                            } else {
                                userRepo.getById(userId).first()
                            }
                            onSuccess(user)
                        }
                    }
                }
            }
        }
    }

    //ToDo Emulate token for password recovery
    private suspend fun sendRecoveryToken(email: String) {}

    //ToDo this is still a pain in the ass
    private fun review(
        login: String,
        password: String,
        rememberMeFor30Days: Boolean
    ) {
        if ((state as SignInState.Main).isValid) {
            state = SignInState.Loading
            viewModelScope.launch {
                val res = PoleServerApi(ktorHttpClient).login(
                    AuthRequestDTO(login, password)
                )
                if (res is Result.Ok && res.data != null) {
                    val rememberUserTill = if (rememberMeFor30Days) {
                        Instant.now().plus(30, ChronoUnit.DAYS).toEpochMilli()
                    }
                    else null
                    prefsRepo.update(
                        userTokens = res.data,
                        rememberUserTill = rememberUserTill
                    )
                    onEvent(SignInEvent.OnSuccess)
                } else {
                    _stateFlow.update {
                        SignInState.Main(
                            login.toFocusable(),
                            password.toFocusable(),
                            rememberMeFor30Days
                        )
                    }
                    _snackbarText.update { (res as Result.Fail).error!! }
                }
            }
        } else {
            with(state as SignInState.Main) {
                state = checkIfValid { _snackbarText.value = UiText.Res(it) }
            }
        }
    }
}
