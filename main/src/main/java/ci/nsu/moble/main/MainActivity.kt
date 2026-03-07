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
import androidx.compose.ui.graphics.Color

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

// Структура данных для хранения цветов
private val colorsMap = mapOf(
    "Red" to Color.Red,
    "Orange" to Color(0xFFFF9800), // Оранжевый
    "Yellow" to Color.Yellow,
    "Green" to Color.Green,
    "Blue" to Color.Blue,
    "Indigo" to Color(0xFF3F51B5), // Индиго
    "Violet" to Color(0xFF9C27B0)  // Фиолетовый
)

@Composable
fun ColorSearchScreen(modifier: Modifier = Modifier) {
    // Состояние для хранения текста из поля ввода
    var text by remember { mutableStateOf("") }
    // Состояние для хранения цвета кнопки (по умолчанию серый)
    var buttonColor by remember { mutableStateOf(Color.Gray) }

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
                // Поиск цвета в структуре данных
                val foundColor = colorsMap[text]

                if (foundColor != null) {
                    // Если цвет найден - меняем цвет кнопки
                    buttonColor = foundColor
                    println("Цвет '$text' найден, применяю к кнопке")
                }
                else {
                    // Если цвет не найден - оставляем серый и пишем в лог
                    buttonColor = Color.Gray
                    println("Пользовательский цвет '$text' не найден")
                }
            },
            // Применяем цвет к кнопке
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Найти цвет",
                // Меняем цвет текста для лучшей читаемости
                color = if (buttonColor == Color.Gray || buttonColor == Color.Yellow || buttonColor == Color(0xFFFF9800))
                    Color.Black else Color.White
            )
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