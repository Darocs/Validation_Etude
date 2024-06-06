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
package ru.polescanner.validation_etude.ui.reusable.util

import androidx.lifecycle.Observer

/**
 * Used as a wrapper for data that is to be exposed via a LiveData
 * only once, e.g. represents a single event - notice.
 */
open class Notice<out T>(private val content: T) {

    @Suppress("MemberVisibilityCanBePrivate")
    var hasBeenDelivered = false
        private set // Allow external read but not write

    /**
     * Deliver the content and prevents its deliver again.
     */
    fun deliver(): T? {
        return if (hasBeenDelivered) {
            null
        } else {
            hasBeenDelivered = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been delivered.
     */
    fun investigate(): T = content

    override fun toString(): String {
        return "Notice of $content"
    }
}

/**
 * An [Observer] for [Notice]s, simplifying the pattern of checking if the [Notice]'s content has
 * already been handled.
 *
 * [onDeliverUnhandledNotice] is *only* called if the [Notice]'s contents has not been handled.
 */
class NoticeObserver<T>(private val onDeliverUnhandledNotice: (T) -> Unit) : Observer<Notice<T>> {
    override fun onChanged(notice: Notice<T>) {
        notice.deliver()?.let {
            onDeliverUnhandledNotice(it)
        }
    }
}
