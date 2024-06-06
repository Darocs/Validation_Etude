package ru.polescanner.validation_etude.ui.reusable.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.polescanner.validation_etude.R


abstract class AbstractViewModel <S: UiState, E: UiEvent>: ViewModel() {

    protected val _snackbarText  = MutableStateFlow<UiText>(UiText.Res(R.string.void_text))
    val snackbarText: StateFlow<UiText> = _snackbarText.asStateFlow()

    protected abstract val _stateFlow: MutableStateFlow<S>
    abstract val stateFlow: StateFlow<S>

    protected open var state: S
        get() = _stateFlow.value
        set(newState) {
            viewModelScope.launch { _stateFlow.update { newState }}
        }

    abstract fun onEvent(e: E)

    fun error() {
        _snackbarText.value = UiText.Res(R.string.smth_get_wrong)
    }
}

