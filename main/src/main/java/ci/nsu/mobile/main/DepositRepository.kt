package ci.nsu.mobile.main

import kotlinx.coroutines.flow.Flow

// класс-посредник между ViewModel и базой данных
class DepositRepository(private val dao: DepositDao) { //принимает DAO при создании и сохраняет в приватное поле

    suspend fun saveCalculation(calculation: DepositCalculation) {
        dao.insert(calculation) //вызывает insert из DAO
    }

    fun getAllCalculations(): Flow<List<DepositCalculation>> {
        return dao.getAllCalculations()
    } //Возвращает поток всех расчётов (автоматически обновляется при изменении БД)

    suspend fun getCalculationById(id: Long): DepositCalculation? {
        return dao.getCalculationById(id) //Возвращает один расчёт по ID или null
    }
}