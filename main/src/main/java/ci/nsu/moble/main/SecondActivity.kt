package ci.nsu.moble.main

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import ci.nsu.moble.main.ui.theme.PracticeTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ci.nsu.moble.main.ui.Screens.HomeScreen
import ci.nsu.moble.main.ui.Screens.ScreenOneContent
import ci.nsu.moble.main.ui.Screens.ScreenTwoContent

// TODO: crate sealed class with 3 routes
// Sealed class (запечатанный класс) - это способ создать ограниченный набор возможных вариантов
sealed class ScreenRoutes(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : ScreenRoutes("home", "Home", Icons.Filled.Home)
    object ScreenOne : ScreenRoutes("screen_one", "Screen One", Icons.Filled.List)
    object ScreenTwo : ScreenRoutes("screen_two", "Screen Two", Icons.Filled.Settings)

    companion object {
        // Список всех экранов для навигации
        val items = listOf(Home, ScreenOne, ScreenTwo)

        // Получить индекс экрана по маршруту
        fun getIndexFromRoute(route: String?): Int {
            return items.indexOfFirst { it.route == route }
        }
    }
}

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                SecondActivityScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondActivityScreen() {
    // todo: create nav controller
    val navController = rememberNavController()

    // Выбранный пункт в нижнем меню теперь синхронизирован с navController
    val context = LocalContext.current
    var receivedText by remember { mutableStateOf("") }

    // Получаем переданные из MainActivity данные
    if (context is Activity) {
        receivedText = context.intent.getStringExtra("text_data") ?: "No text received"
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(receivedText) },
                navigationIcon = {
                    IconButton(onClick = {
                        // Просто закрываем текущую активность, возвращаемся в MainActivity
                        if (context is Activity) {
                            context.finish()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar {
                // Используем цикл для создания пунктов меню
                ScreenRoutes.items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(imageVector = screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = navController.currentDestination?.route == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Паттерн для BottomNavigation
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // create a nav graph with 3 screens
        NavHost(
            navController = navController,
            startDestination = ScreenRoutes.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(ScreenRoutes.Home.route) {
                HomeScreen()
            }
            composable(ScreenRoutes.ScreenOne.route) {
                ScreenOneContent()
            }
            composable(ScreenRoutes.ScreenTwo.route) {
                ScreenTwoContent()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PracticeTheme {
        SecondActivityScreen()
    }
}