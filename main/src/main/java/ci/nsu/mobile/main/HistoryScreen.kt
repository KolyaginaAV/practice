package ci.nsu.mobile.main

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.Alignment

@Composable
fun HistoryScreen(
    onNavigateToDetail: (Long) -> Unit,
    viewModel: HistoryViewModel = viewModel()
) {
    // Получаем список расчётов из ViewModel
    val calculations by viewModel.allCalculations.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "История расчётов",
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (calculations.isEmpty()) {
            // Если список пуст
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Нет сохранённых расчётов",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            // Список расчётов
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(calculations) { calculation ->
                    Log.d("HistoryScreen", "Рисуем элемент с ID: ${calculation.id}")
                    HistoryItem(
                        calculation = calculation,
                        onClick = {
                            Log.d("HistoryScreen", "Клик по элементу с ID: ${calculation.id}")
                            onNavigateToDetail(calculation.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryItem(
    calculation: DepositCalculation,
    onClick: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // ВРЕМЕННО: показать ID
                Text(
                    text = "ID: ${calculation.id}",  // ← ДОБАВИТЬ ЭТУ СТРОКУ
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = String.format("%.2f ₽", calculation.initialAmount),
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                Text(
                    text = "→ ${String.format("%.2f ₽", calculation.finalAmount)}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${calculation.periodMonths} мес.",
                    fontSize = 14.sp
                )
                Text(
                    text = dateFormat.format(Date(calculation.calculationDate)),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}