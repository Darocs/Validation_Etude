package ru.polescanner.validation_etude.domain.general

import arrow.core.Either

// ToDo Module is compiled how to provide constants in runtime
@JvmInline
value class Login private constructor(override val value: String) : Name {
    companion object {
        operator fun invoke(login: String): Either<Name.ValidationError, Login> = Login(login.trim())
            .validated(
                minLen = DI.login?.min ?: 5,
                maxLen = DI.login?.max ?: 10,
                validCharsRegex = DI.login?.regex ?: "[a-zA-Z0-9]+"
            )
    }
}

@JvmInline
value class Password private constructor(override val value: String) : Name {
    companion object {
        operator fun invoke(password: String): Either<Name.ValidationError, Password> =
            Password(password.trim())
                .validated(
                    minLen = DI.password?.min ?: 5,
                    maxLen = DI.password?.max ?: 10,
                    validCharsRegex = DI.password?.regex ?: "[a-zA-Z0-9]+"
                )
    }
}