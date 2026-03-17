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
sealed class ScreenRoutes(val route: String) {
    object Home : ScreenRoutes("home")
    object ScreenOne : ScreenRoutes("screen_one")
    object ScreenTwo : ScreenRoutes("screen_two")
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
    val navController = rememberNavController() //Навигационный контроллер - "пульт управления" переключением экранов

    var selectedItem by remember { mutableStateOf(0) } //Выбранный пункт в нижнем меню (0 - Home, 1 - Screen One, 2 - Screen Two)
    val context = LocalContext.current //lоступ к контексту Android (чтобы работать с Intent и активностью)
    var receivedText by remember { mutableStateOf("") } //текст, переданный из mainactivity

    // Получаем переданные из MainActivity данные
    if (context is Activity) {
        receivedText = context.intent.getStringExtra("text_data") ?: "No text received"
    }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = { Text(receivedText) }, navigationIcon = {
                IconButton(onClick = {
                    // TODO: create intent and start MainActivity
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
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Blue, titleContentColor = Color.White
            )
        )
    }, bottomBar = {
        NavigationBar {
            NavigationBarItem(
                icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = "Home") },
                label = { Text("Home") },
                selected = selectedItem == 0,

                onClick = {
                    // TODO: navigate to home screen by navController
                    navController.navigate(ScreenRoutes.Home.route) {
                        //паттерн для BottomNavigation
                        // Очищаем стек до начального пункта назначения
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true //сохраняем состояние текущего экрана
                        }
                        launchSingleTop = true //launchSingleTop - не создаем дубликаты экранов
                        restoreState = true //restoreState - восстанавливаем состояние, если возвращаемся
                    }
                    selectedItem = 0
                })
            NavigationBarItem(
                icon = { Icon(imageVector = Icons.Filled.List, contentDescription = "Screen One") },
                label = { Text("Screen One") },
                selected = selectedItem == 1,

                onClick = {
                    // TODO: navigate to screen one
                    navController.navigate(ScreenRoutes.ScreenOne.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    selectedItem = 1
                })
            NavigationBarItem(
                icon = { Icon(imageVector = Icons.Filled.Settings, contentDescription = "Screen Two") },
                label = { Text("Screen Two") },
                selected = selectedItem == 2,
                onClick = {
                    // TODO: navigate to screen two
                    navController.navigate(ScreenRoutes.ScreenTwo.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    selectedItem = 2
                })
        }
    }) { innerPadding ->
        // TODO: create a nav graph with 3 screens - create a nav graph with 3 screens
        NavHost(
            navController = navController,
            startDestination = ScreenRoutes.Home.route, // Стартовый экран
            modifier = Modifier.padding(innerPadding) // Отступы от панелей
        ) {
            composable(ScreenRoutes.Home.route) {  // Когда маршрут "home" - показываем HomeScreen
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