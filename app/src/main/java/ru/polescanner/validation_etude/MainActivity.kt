package ru.polescanner.validation_etude

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.polescanner.validation_etude.ui.theme.Validation_EtudeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Validation_EtudeTheme {
                ValidationEtude()
            }
        }
    }
}
