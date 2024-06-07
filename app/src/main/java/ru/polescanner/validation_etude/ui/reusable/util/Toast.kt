package ru.polescanner.validation_etude.ui.reusable.util

import android.content.Context
import android.widget.Toast

fun makeToast(context: Context,  text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}