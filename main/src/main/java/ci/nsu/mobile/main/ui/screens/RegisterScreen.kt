package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.viewmodel.RegisterViewModel
import ci.nsu.mobile.main.viewmodel.RegisterViewModelFactory
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.network.RetrofitInstance
import ci.nsu.mobile.main.utils.TokenManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val apiService = remember { RetrofitInstance.create(tokenManager) }
    val authRepository = remember { AuthRepository(apiService, tokenManager) }
    val registerViewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModelFactory(authRepository)
    )

    val uiState by registerViewModel.uiState.collectAsState()
    val groups by registerViewModel.groups.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState) // позволяет скроллить форму, когда она не помещается на экране
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Регистрация",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.lastName,
            onValueChange = { registerViewModel.updateField("lastName", it) },
            label = { Text("Фамилия") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.firstName,
            onValueChange = { registerViewModel.updateField("firstName", it) },
            label = { Text("Имя") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.middleName,
            onValueChange = { registerViewModel.updateField("middleName", it) },
            label = { Text("Отчество") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.birthDate,
            onValueChange = { registerViewModel.updateField("birthDate", it) },
            label = { Text("Дата рождения (ГГГГ-ММ-ДД)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.gender,
            onValueChange = { registerViewModel.updateField("gender", it) },
            label = { Text("Пол (MALE/FEMALE)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = groups.find { it.id == uiState.selectedGroupId }?.name ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Группа") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                //ExposedDropdownMenuBox - выпадающий список для выбора группы
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                enabled = !uiState.isLoading
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                groups.forEach { group ->
                    DropdownMenuItem(
                        text = { Text(group.name) },
                        onClick = {
                            registerViewModel.selectGroup(group.id)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.login,
            onValueChange = { registerViewModel.updateField("login", it) },
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.password,
            onValueChange = { registerViewModel.updateField("password", it) },
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.email,
            onValueChange = { registerViewModel.updateField("email", it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.phoneNumber,
            onValueChange = { registerViewModel.updateField("phoneNumber", it) },
            label = { Text("Телефон") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { registerViewModel.register(onRegisterSuccess) },
                modifier = Modifier.fillMaxWidth(),
                enabled = true  // временно для теста
            ) {
                Text("Зарегистрироваться")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onNavigateToLogin,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) {
            Text("Уже есть аккаунт? Войти")
        }

        if (uiState.error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = uiState.error ?: "",
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}