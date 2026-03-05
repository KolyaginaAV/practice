package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ColorSearchScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ColorSearchScreen(modifier: Modifier = Modifier) {
    // Состояние для хранения текста из поля ввода
    var text by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Текстовое поле для ввода
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Введите название цвета") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка (пока просто серая)
        Button(
            onClick = {
                // Здесь потом будет логика
                println("Нажали кнопку! Текст: $text")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Найти цвет")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ColorSearchScreenPreview() {
    PracticeTheme {
        ColorSearchScreen()
    }
}