package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.models.RegisterRequest
import ci.nsu.mobile.main.data.models.UserDto
import ci.nsu.mobile.main.data.models.GroupDto
import ci.nsu.mobile.main.data.network.ApiService
import ci.nsu.mobile.main.utils.TokenManager

// управление данными - посредник между ViewModel и источниками данных (API, БД и т.д.)

class AuthRepository(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {

    suspend fun login(login: String, password: String): Result<String> {
        return try {
            val request = mapOf("login" to login, "password" to password)
            val response = apiService.login(request)     // запрос к серверу
            tokenManager.token = response.token          // сохраняем токен
            Result.success(response.token)               // возвращаем успех
        } catch (e: Exception) {
            Result.failure(e)                            // возвращаем ошибку
        }
    }

    suspend fun register(registerRequest: RegisterRequest): Result<Unit> {
        return try {
            apiService.register(registerRequest)         // запрос к серверу
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val users = apiService.getUsers()
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val groups = apiService.getGroups()
            Result.success(groups)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        tokenManager.clear() // удаляем токен
    }
}