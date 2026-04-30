package ci.nsu.mobile.main

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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
                    navController.navigate("input_second?amountStr=${amount}&periodStr=${period}")
                }
            )
        }

        composable("input_second?amountStr={amountStr}&periodStr={periodStr}") { backStackEntry ->
            val amountStr = backStackEntry.arguments?.getString("amountStr") ?: "0"
            val periodStr = backStackEntry.arguments?.getString("periodStr") ?: "0"

            val amount = amountStr.toDoubleOrNull() ?: 0.0
            val period = periodStr.toIntOrNull() ?: 0

            InputSecondScreen(
                initialAmount = amount,
                periodMonths = period,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToResult = { topUp, rate ->
                    navController.navigate("result?amountStr=${amount}&periodStr=${period}&rateStr=${rate}&topUpStr=${topUp}")
                }
            )
        }

        composable(
            route = "result?amountStr={amountStr}&periodStr={periodStr}&rateStr={rateStr}&topUpStr={topUpStr}"
        ) { backStackEntry ->
            val amountStr = backStackEntry.arguments?.getString("amountStr") ?: "0"
            val periodStr = backStackEntry.arguments?.getString("periodStr") ?: "0"
            val rateStr = backStackEntry.arguments?.getString("rateStr") ?: "0"
            val topUpStr = backStackEntry.arguments?.getString("topUpStr") ?: "0"

            val amount = amountStr.toDoubleOrNull() ?: 0.0
            val period = periodStr.toIntOrNull() ?: 0
            val rate = rateStr.toDoubleOrNull() ?: 0.0
            val topUp = topUpStr.toDoubleOrNull() ?: 0.0

            ResultScreen(
                initialAmount = amount,
                periodMonths = period,
                interestRate = rate,
                monthlyTopUp = topUp,
                onSave = {
                    // Сохранение расчёта (будет позже)
                },
                onBackToMain = {
                    navController.popBackStack(Screen.Main.route, inclusive = false)
                }
            )
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