package com.kitchino.vkproj

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class SecondActivity : ComponentActivity() {

    companion object {
        const val EXTRA_TEXT = "extra_text"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val receivedText = intent.getStringExtra(EXTRA_TEXT)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SecondScreen(receivedText)
                }
            }
        }
    }
}

@Composable
fun SecondScreen(text: String?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Полученный текст:",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = text?.takeIf { it.isNotBlank() } ?: "Текст не был передан",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SecondScreenPreview() {
    MaterialTheme { SecondScreen("Пример текста") }
}