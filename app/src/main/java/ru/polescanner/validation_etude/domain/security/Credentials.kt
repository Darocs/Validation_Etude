package ru.polescanner.validation_etude.domain.security

import ru.polescanner.validation_etude.domain.general.Login
import ru.polescanner.validation_etude.domain.general.Password

data class Credentials(val login: Login, val password: Password, val rememberMe: Boolean)