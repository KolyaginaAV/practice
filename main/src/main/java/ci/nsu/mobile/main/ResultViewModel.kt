package ci.nsu.mobile.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

//класс для управления UI-данными
class ResultViewModel(application: Application) : AndroidViewModel(application) {
    //(application: Application) — принимает контекст приложения
    //: AndroidViewModel(application) — наследуется от AndroidViewModel (имеет доступ к контексту)
    private val database = AppDatabase.getDatabase(application) //Получаем экземпляр базы данных
    private val repository = DepositRepository(database.depositDao()) //Создаём репозиторий, передаём ему DAO

    fun saveCalculation( //Функция сохранения расчёта
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double,
        finalAmount: Double,
        interestEarned: Double,
        onSuccess: () -> Unit //onSuccess: () -> Unit — callback, который выполнится после сохранения
    ) {
        viewModelScope.launch {
            val calculation = DepositCalculation( //Создаём объект для сохранения
                initialAmount = initialAmount,
                periodMonths = periodMonths,
                interestRate = interestRate,
                monthlyTopUp = monthlyTopUp,
                finalAmount = finalAmount,
                interestEarned = interestEarned
            )
            repository.saveCalculation(calculation) //Сохраняем через репозиторий
            onSuccess()
        }
    }
}