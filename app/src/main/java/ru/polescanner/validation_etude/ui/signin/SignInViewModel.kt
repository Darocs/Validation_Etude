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
import ru.polescanner.validation_etude.domain.security.Credentials
import ru.polescanner.validation_etude.ui.reusable.util.AbstractViewModel
import ru.polescanner.validation_etude.ui.reusable.util.UiText
import ru.polescanner.validation_etude.ui.reusable.util.clearFocus
import ru.polescanner.validation_etude.ui.reusable.util.toFocusable
import ru.polescanner.validation_etude.ui.reusable.util.toNullableFocusable
import ru.polescanner.validation_etude.ui.signin.SignInEvent.*
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
                    onEvent(OnSuccess)
                }
                    ?: _stateFlow.update { SignInState.Main() }
            } else {
                _stateFlow.update { SignInState.Main() }
            }
        }
    }
    private fun toServer(c: Credentials): Unit = TODO()

    private fun state() = state as SignInState.Main
    override fun onEvent(e: SignInEvent) {
        when (e) { //MainScreen editing
            is OnLoginChanged -> state =
                state().clearFocus().copy(login = e.login.toFocusable())

            is OnPasswordChanged -> state =
                state().clearFocus().copy(password = e.password.toFocusable())

            is OnRememberMeFor30DaysChanged -> state =
                state().clearFocus().copy(isLoggedIn = e.remember.toNullableFocusable())

            //MainScreen actions click
            // ToDo Check that we don't use e content but use state!!!!
            is OnLogin -> state()
                .toCredentials{ _snackbarText.value = it }
                .fold({ state = it as SignInState.Main }, ::toServer)

            OnLicense -> state = SignInState.License

            OnForgotPassword -> state = SignInState.ForgotPassword()

            //License
            OnLicenseAccept -> state =
                SignInState.Main() //ToDo with license we loose all data like login...
            //ToDo maybe we have to remember date and time when License was accepted?

            //Final transit to PoleDroid
            is OnSuccess -> {
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
                    onEvent(OnSuccess)
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
