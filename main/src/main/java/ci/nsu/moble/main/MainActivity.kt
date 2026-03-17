package ci.nsu.moble.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Вызывается при создании активности
        enableEdgeToEdge() //Делает интерфейс во весь экран
        setContent { //использование Compose для отрисовки интерфейса
            PracticeTheme { //кастомная тема
                //Scaffold - это "каркас" экрана (Он предоставляет готовые места (слоты) для типовых
                //элементов интерфейса)
                //modifier = Modifier.fillMaxSize() - "растянись на весь доступный экран".
                //innerPadding -> - лямбда с параметром (нужен, чтобы контент не накладывался на системные
                // элементы интерфейса)
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreenActivity(
                        modifier = Modifier.padding(innerPadding) //берем те отступы,
                                // которые нам дал Scaffold, и применяем их к нашему контенту.
                    )
                }
            }
        }
    }
}
// TODO:  here is to open the second activity
@Composable
fun MainScreenActivity(modifier: Modifier = Modifier) { //Это твой главный экран в первой активности
    var text by remember { mutableStateOf("") } //переменная, чтобы хранить то, что пользователь
    // введет в поле; remember сохраняет значение при перерисовках (рекомпозиции) экрана
    val context = LocalContext.current // Получает текущий Context. Нужен для запуска новой активности

    Column( //Размещает элементы вертикально
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TODO:  нужно добавить  TextField
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Введите текст для передачи") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                // TODO:  нужно добавить кнопку которая по клику открывает второе активити через интент
                // Создаем явный Intent для перехода во SecondActivity
                val intent = Intent(context, SecondActivity::class.java).apply {
                    // Передаем строку с ключом "text_data"
                    putExtra("text_data", text)
                }
                context.startActivity(intent)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Open SecondActivity")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PracticeTheme {
        MainScreenActivity()
    }
}