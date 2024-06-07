package ru.polescanner.validation_etude.ui.reusable.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.polescanner.validation_etude.ui.reusable.util.UiText

@Composable
fun LoadingScreen(
    label: UiText = UiText.Str("")
) { //ToDo make a common LoadingScreen - maybe copy from CodeLab
    Box(
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(60.dp)) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                trackColor = MaterialTheme.colorScheme.secondary,
            )
            Text(text = label.asString(),
                 fontSize = 20.sp,)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoadingScreenPreview() {
    LoadingScreen(label = UiText.Str("Hello World"))
}