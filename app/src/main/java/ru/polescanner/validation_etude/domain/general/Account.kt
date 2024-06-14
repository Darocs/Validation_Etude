package ru.polescanner.validation_etude.domain.general

import arrow.core.Either

// ToDo Module is compiled how to provide constants in runtime
@JvmInline
value class Login private constructor(override val value: String) : Name {
    companion object {
        operator fun invoke(login: String): Either<Name.ValidationError, Login> = Login(login.trim())
            .validated(
                minLen = 5,
                maxLen = 10,
                validCharsRegex = "[a-zA-Z0-9]+"
            )
    }
}

@JvmInline
value class Password private constructor(override val value: String) : Name {
    companion object {
        operator fun invoke(password: String): Either<Name.ValidationError, Password> =
            Password(password.trim())
                .validated(
                    minLen = 5,
                    maxLen = 10,
                    validCharsRegex = "[a-zA-Z0-9]+"
                )
    }
}