package ci.nsu.mobile.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResultScreen(
    initialAmount: Double,
    periodMonths: Int,
    interestRate: Double,
    monthlyTopUp: Double,
    onSave: () -> Unit,
    onBackToMain: () -> Unit
) {
    // Вызываем функцию расчёта
    val (finalAmount, interestEarned) = calculateDeposit(
        initialAmount = initialAmount,
        periodMonths = periodMonths,
        annualRate = interestRate,
        monthlyTopUp = monthlyTopUp
    )

    // Основной контейнер (вертикальная колонка)
    Column(
        modifier = Modifier
            .fillMaxSize()           // на весь экран
            .padding(16.dp),        // отступы со всех сторон
        verticalArrangement = Arrangement.spacedBy(16.dp)  // расстояние между элементами
    ) {
        // Заголовок
        Text(
            text = "Результат расчёта",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        // Карточка с результатами
        Card(
            modifier = Modifier.fillMaxWidth(),  // карточка на всю ширину
            shape = RoundedCornerShape(16.dp),   // скруглённые углы
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)  // тень
        ) {
            // Внутренности карточки
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Строка: Стартовый взнос
                InfoRow("Стартовый взнос:", String.format("%.2f ₽", initialAmount))

                // Строка: Срок вклада
                InfoRow("Срок вклада:", "$periodMonths мес.")

                // Строка: Процентная ставка
                InfoRow("Процентная ставка:", String.format("%.1f %%", interestRate))

                // Строка: Ежемесячное пополнение (только если > 0)
                if (monthlyTopUp > 0) {
                    InfoRow("Ежемесячное пополнение:", String.format("%.2f ₽", monthlyTopUp))
                }

                // Разделительная линия
                Divider()

                // Строка: Итоговая сумма (жирным шрифтом)
                InfoRow(
                    "Итоговая сумма:",
                    String.format("%.2f ₽", finalAmount),
                    isBold = true,
                    textColor = MaterialTheme.colorScheme.primary
                )

                // Строка: Начисленные проценты
                InfoRow(
                    "Начисленные проценты:",
                    String.format("%.2f ₽", interestEarned),
                    isBold = true
                )
            }
        }

        // "Распорка" - занимает всё свободное место, прижимая кнопки вниз
        Spacer(modifier = Modifier.weight(1f))

        // Кнопки в ряд
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Кнопка "Сохранить"
            Button(
                onClick = onSave,
                modifier = Modifier.weight(1f)  // занимает половину ширины
            ) {
                Text("Сохранить")
            }

            // Кнопка "В начало"
            Button(
                onClick = onBackToMain,
                modifier = Modifier.weight(1f)
            ) {
                Text("В начало")
            }
        }
    }
}

// Вспомогательный компонент для отображения одной строки (название + значение)
@Composable
fun InfoRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween  // название слева, значение справа
    ) {
        Text(
            text = label,
            fontSize = if (isBold) 16.sp else 14.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            fontSize = if (isBold) 16.sp else 14.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = textColor
        )
    }
}

// Функция расчёта вклада (формула)
fun calculateDeposit(
    initialAmount: Double,
    periodMonths: Int,
    annualRate: Double,
    monthlyTopUp: Double
): Pair<Double, Double> {
    // Месячная процентная ставка (например, 5% годовых = 5/100/12 = 0.004166...)
    val monthlyRate = annualRate / 100 / 12

    var total = initialAmount  // начинаем с начальной суммы

    // Для каждого месяца
    for (month in 1..periodMonths) {
        total += total * monthlyRate  // начисляем проценты на текущую сумму
        if (monthlyTopUp > 0) {
            total += monthlyTopUp      // добавляем пополнение (в конце месяца)
        }
    }

    // Начисленные проценты = итог - начальный взнос - все пополнения
    val interestEarned = total - initialAmount - (monthlyTopUp * periodMonths)

    // Возвращаем пару чисел: (итоговая сумма, начисленные проценты)
    return Pair(total, interestEarned)
}