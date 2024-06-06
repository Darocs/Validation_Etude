/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//from Joce Alcerreca in testing codelab

package ru.polescanner.validation_etude.domainext.bus

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out O, out E>{
    data class Ok<out O>(val data: O? = null): Result<O, Nothing>()
    data class Fail<out E>(val error: E? = null): Result<Nothing, E>()

    object Loading: Result<Nothing, Nothing>()

    override fun toString(): String {
        return when (this) {
            is Ok<*> -> "Success[data=$data]"
            is Fail -> "Error[exception=$error]"
            Loading -> "Loading"
        }
    }
}

/**
 * `true` if [Result] is of type [Result.Ok] & holds non-null [Result.Ok.data].
 */
val Result<*, *>.succeeded
    get() = this is Result.Ok && data != null
