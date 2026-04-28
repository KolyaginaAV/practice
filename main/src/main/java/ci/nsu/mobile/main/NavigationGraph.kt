package ci.nsu.mobile.main

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object InputFirst : Screen("input_first")
    object InputSecond : Screen("input_second")
    object Result : Screen("result")
    object History : Screen("history")
    object HistoryDetail : Screen("history_detail/{calculationId}") {
        fun passId(id: Long): String = "history_detail/$id"
    }
}

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(
                onNavigateToInput = { navController.navigate(Screen.InputFirst.route) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) }
            )
        }

        composable(Screen.InputFirst.route) {
            InputFirstScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSecond = { amount, period ->
                    Toast.makeText(context, "NavigationGraph: сумма=$amount, срок=$period", Toast.LENGTH_SHORT).show()
                    // Передаём числа как строки в URL
                    navController.navigate("input_second?amountStr=${amount}&periodStr=${period}")
                }
            )
        }

        composable("input_second?amountStr={amountStr}&periodStr={periodStr}") { backStackEntry ->
            // Получаем как строки из URL
            val amountStr = backStackEntry.arguments?.getString("amountStr") ?: "0"
            val periodStr = backStackEntry.arguments?.getString("periodStr") ?: "0"

            // Преобразуем строки в числа
            val amount = amountStr.toDoubleOrNull() ?: 0.0
            val period = periodStr.toIntOrNull() ?: 0

            Toast.makeText(context, "NavigationGraph получил из URL: сумма=$amount, срок=$period", Toast.LENGTH_SHORT).show()

            InputSecondScreen(
                initialAmount = amount,
                periodMonths = period,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToResult = { topUp, rate ->
                    navController.navigate("result?amountStr=${amount}&periodStr=${period}&rateStr=${rate}&topUpStr=${topUp}")
                }
            )
        }

        composable("result?amountStr={amountStr}&periodStr={periodStr}&rateStr={rateStr}&topUpStr={topUpStr}") { backStackEntry ->
            val amountStr = backStackEntry.arguments?.getString("amountStr") ?: "0"
            val periodStr = backStackEntry.arguments?.getString("periodStr") ?: "0"
            val rateStr = backStackEntry.arguments?.getString("rateStr") ?: "0"
            val topUpStr = backStackEntry.arguments?.getString("topUpStr") ?: "0"

            val amount = amountStr.toDoubleOrNull() ?: 0.0
            val period = periodStr.toIntOrNull() ?: 0
            val rate = rateStr.toDoubleOrNull() ?: 0.0
            val topUp = topUpStr.toDoubleOrNull() ?: 0.0

            Text(text = "Результат: ${amount} ₽, ${period} мес., ${rate}%, пополнение ${topUp} ₽")
        }

        composable(Screen.History.route) {
            Text(text = "Экран истории (будет позже)")
        }

        composable(Screen.HistoryDetail.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("calculationId") ?: 0
            Text(text = "Детали расчёта #${id} (будет позже)")
        }
    }
}