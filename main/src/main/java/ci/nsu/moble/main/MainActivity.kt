package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
// Data class для цвета с названием
//data class - класс, который автоматически получает equals(), hashCode(), toString()
//equals() - сравнивает два объекта по их содержимому, а не по ссылке в памяти
//hashCode() - создаёт уникальное число на основе содержимого объекта
//Если equals говорит что объекты равны, то hashCode должен быть одинаковым. data class гарантирует это.
//toString() - превращает объект в строку для чтения человеком
data class ColorItem(
    val name: String,
    val color: Color
)

// Структура данных для хранения цветов
private val colorsMap = mapOf( //mapOf - создаёт неизменяемую карту (словарь)
    "Red" to Color.Red,
    "Orange" to Color(0xFFFF9800), // Оранжевый - создание цвета из HEX-кода (0x - шестнадцатеричный формат, FF9800 - оранжевый)
    "Yellow" to Color.Yellow,
    "Green" to Color.Green,
    "Blue" to Color.Blue,
    "Indigo" to Color(0xFF3F51B5), // Индиго
    "Violet" to Color(0xFF9C27B0)  // Фиолетовый
)

// Список цветов для отображения палитры
private val colorsList = listOf(    //listOf - создаёт неизменяемый список
    ColorItem("Red", Color.Red),
    ColorItem("Orange", Color(0xFFFF9800)),
    ColorItem("Yellow", Color.Yellow),
    ColorItem("Green", Color.Green),
    ColorItem("Blue", Color.Blue),
    ColorItem("Indigo", Color(0xFF3F51B5)),
    ColorItem("Violet", Color(0xFF9C27B0))
)

@Composable //аннотация, означающая что это UI-компонент (User Interface)
fun ColorSearchScreen(modifier: Modifier = Modifier) {
    // Состояние для хранения текста из поля ввода
    var text by remember { mutableStateOf("") }
    // Состояние для хранения цвета кнопки (по умолчанию серый)
    var buttonColor by remember { mutableStateOf(Color.Gray) }

    Column(  //вертикальное расположение элементов
        modifier = modifier //настройки внешнего вида и поведения
            .fillMaxSize()  //занимает всё доступное пространство
            .padding(16.dp), //отступы от краёв
        horizontalAlignment = Alignment.CenterHorizontally, //выравнивание по центру по горизонтали
    ) {
        // Текстовое поле для ввода
        OutlinedTextField(
            value = text, //текущее значение (берётся из состояния)
            onValueChange = { text = it }, //что делать при изменении текста (обновляем состояние); it - новый текст
            label = { Text("Введите название цвета") }, //подпись над полем
            modifier = Modifier.fillMaxWidth(),
            singleLine = true //только одна строка
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка (пока просто серая)
        Button(
            onClick = {
                // Поиск цвета в структуре данных (по ключу (названию))
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
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Найти цвет",
                // Меняем цвет текста для лучшей читаемости
                color = if (buttonColor == Color.Gray || buttonColor == Color.Yellow || buttonColor == Color(0xFFFF9800))
                    Color.Black else Color.White
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Заголовок для палитры
        Text(
            text = "Палитра цветов:",
            style = MaterialTheme.typography.titleLarge //стандартный стиль из темы
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Column со всеми цветами
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp) //расстояние между элементами
        ) {
            colorsList.forEach { colorItem -> //перебираем все цвета и для каждого вызываем функцию ColorListItem
                ColorListItem(colorItem)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
@Composable
fun ColorListItem(colorItem: ColorItem) {
    Card(   //карточка с фоном цвета
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorItem.color
        )
    ) {
        Row(    //горизонтальное расположение (название слева, HEX справа)
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween //максимальное расстояние между элементами
        ) {
            Text( //контрастный цвет текста
                text = colorItem.name,
                color = if (colorItem.color == Color.Yellow || colorItem.color == Color(0xFFFF9800))
                    Color.Black else Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = colorToHex(colorItem.color),
                color = if (colorItem.color == Color.Yellow || colorItem.color == Color(0xFFFF9800))
                    Color.Black else Color.White
            )
        }
    }
}

// Вспомогательная функция для преобразования Color в HEX
fun colorToHex(color: Color): String {
    return String.format(
        "#%02X%02X%02X", //%02X - шестнадцатеричное число с ведущим нулём
        (color.red * 255).toInt(), //переводит из 0.0-1.0 в 0-255
        (color.green * 255).toInt(),
        (color.blue * 255).toInt()
    )
}

@Preview(showBackground = true)
@Composable
fun ColorSearchScreenPreview() {
    PracticeTheme {
        ColorSearchScreen()
    }
}