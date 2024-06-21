package ru.polescanner.validation_etude

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ru.polescanner.validation_etude.ui.signin.SignInRoute

@Composable
fun ValidationEtude(modifier: Modifier = Modifier) {
    val snackbarHostState = remember { SnackbarHostState() }
    CompositionLocalProvider(
        LocalSnackbarHostState provides snackbarHostState,
        ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->
            SignInRoute(modifier = modifier.padding(padding).fillMaxSize())
        }
    }
}

val LocalSnackbarHostState =
    compositionLocalOf<SnackbarHostState> { error("No SnackbarHostState provided") }
