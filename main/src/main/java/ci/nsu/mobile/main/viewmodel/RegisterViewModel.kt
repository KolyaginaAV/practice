package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.models.GroupDto
import ci.nsu.mobile.main.data.models.PersonDto
import ci.nsu.mobile.main.data.models.RegisterRequest
import ci.nsu.mobile.main.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//Экран регистрации - Логика регистрации + загрузка групп
class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Состояние UI (что видит пользователь)
    private val _uiState = MutableStateFlow(RegisterUiState())
    //MutableStateFlow - изменяемый поток
    val uiState: StateFlow<RegisterUiState> = _uiState
    //StateFlow - поток данных, на который может подписаться UI

    private val _groups = MutableStateFlow<List<GroupDto>>(emptyList())
    val groups: StateFlow<List<GroupDto>> = _groups

    init {
        loadGroups() //// Загружаем группы с сервера при создании ViewModel
    }

    fun updateField(field: String, value: String) { //Обновляет одно поле в форме
        when (field) {
            "firstName" -> _uiState.value = _uiState.value.copy(firstName = value)
            "lastName" -> _uiState.value = _uiState.value.copy(lastName = value)
            "middleName" -> _uiState.value = _uiState.value.copy(middleName = value)
            "birthDate" -> _uiState.value = _uiState.value.copy(birthDate = value)
            "gender" -> _uiState.value = _uiState.value.copy(gender = value)
            "login" -> _uiState.value = _uiState.value.copy(login = value)
            "password" -> _uiState.value = _uiState.value.copy(password = value)
            "email" -> _uiState.value = _uiState.value.copy(email = value)
            "phoneNumber" -> _uiState.value = _uiState.value.copy(phoneNumber = value)
        }
    }

    //Выбирает группу из выпадающего списка
    fun selectGroup(groupId: Int) {
        _uiState.value = _uiState.value.copy(selectedGroupId = groupId)
    }

    //Отправляет запрос на регистрацию
    fun register(onSuccess: () -> Unit) {
        viewModelScope.launch {
            //Показываем прогресс и убираем ошибку
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            //Собираем данные о человеке из формы
            val person = PersonDto(
                firstName = _uiState.value.firstName,
                lastName = _uiState.value.lastName,
                middleName = _uiState.value.middleName,
                birthDate = _uiState.value.birthDate,
                gender = _uiState.value.gender,
                groupId = _uiState.value.selectedGroupId
            )

            // Собираем полный запрос на регистрацию
            val request = RegisterRequest(
                login = _uiState.value.login,
                password = _uiState.value.password,
                email = _uiState.value.email,
                phoneNumber = _uiState.value.phoneNumber,
                roleId = 1,           // Всегда 1 (по заданию)
                authAllowed = true,   // Всегда true (по заданию)
                person = person
            )

            // Отправляем запрос на сервер
            val result = authRepository.register(request)

            //Обрабатываем результат
            result.onSuccess {
                // Успех: убираем прогресс и переходим на экран входа
                _uiState.value = _uiState.value.copy(isLoading = false)
                onSuccess()
            }.onFailure { exception ->
                // Ошибка: убираем прогресс и показываем сообщение
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Ошибка регистрации"
                )
            }
        }
    }

    //Загружает список групп с сервера
    private fun loadGroups() {
        viewModelScope.launch {
            val result = authRepository.getGroups()
            result.onSuccess { groups ->
                _groups.value = groups // Сохраняем группы в отдельный поток
            }.onFailure {
                // Если не удалось загрузить группы - показываем ошибку
                _uiState.value = _uiState.value.copy(
                    error = "Не удалось загрузить группы"
                )
            }
        }
    }
}

//RegisterUiState - состояние экрана регистрации
data class RegisterUiState(
    val firstName: String = "",      // Имя
    val lastName: String = "",       // Фамилия
    val middleName: String = "",     // Отчество
    val birthDate: String = "",      // Дата рождения
    val gender: String = "",         // Пол (MALE/FEMALE)
    val login: String = "",          // Логин
    val password: String = "",       // Пароль
    val email: String = "",          // Email
    val phoneNumber: String = "",    // Телефон
    val selectedGroupId: Int = 0,    // ID выбранной группы (0 = не выбрана)
    val isLoading: Boolean = false,  // Показывать прогресс?
    val error: String? = null        // Текст ошибки
)