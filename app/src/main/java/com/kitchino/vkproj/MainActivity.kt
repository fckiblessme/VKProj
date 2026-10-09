package com.kitchino.vkproj

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Демонстрация Intent",
                style = MaterialTheme.typography.headlineSmall
            )

            // --- Поле для текста (используется в явном и системном Intent) ---
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Введите текст") },
                modifier = Modifier.fillMaxWidth(),
                isError = text.isBlank()
            )

            // --- Поле для телефона ---
            OutlinedTextField(
                value = phone,
                onValueChange = { input ->
                    // Разрешаем только цифры и служебные символы
                    if (input.all { it.isDigit() || it in "+- ()" }) {
                        phone = input
                    }
                },
                label = { Text("Номер телефона") },
                placeholder = { Text("+7 999 123-45-67") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                isError = phone.isNotBlank() && !isValidPhone(phone)
            )

            // 1. ЯВНЫЙ INTENT
            Button(
                onClick = {
                    if (text.isBlank()) {
                        Toast.makeText(context, "Введите текст", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val intent = Intent(context, SecondActivity::class.java).apply {
                        putExtra(SecondActivity.EXTRA_TEXT, text)
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Открыть вторую Activity")
            }

            // 2. НЕЯВНЫЙ INTENT — ACTION_DIAL
            Button(
                onClick = {
                    if (!isValidPhone(phone)) {
                        Toast.makeText(context, "Введите корректный номер", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phone.trim()}"))
                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Приложение звонков не найдено", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Позвонить другу")
            }

            // 3. СИСТЕМНЫЙ INTENT — ACTION_SEND
            Button(
                onClick = {
                    if (text.isBlank()) {
                        Toast.makeText(context, "Введите текст для отправки", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, text)
                    }
                    val chooser = Intent.createChooser(sendIntent, "Поделиться через…")
                    try {
                        context.startActivity(chooser)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Нет приложений для отправки", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Поделиться текстом")
            }
        }
    }
}

private fun isValidPhone(phone: String): Boolean {
    val digits = phone.filter { it.isDigit() }
    return digits.length in 5..15
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MaterialTheme { MainScreen() }
}