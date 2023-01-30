package com.github.zuev98.currencyconverter.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.zuev98.currencyconverter.domain.usecases.GetCurrenciesUseCase
import com.github.zuev98.currencyconverter.domain.usecases.GetLastUpdateUseCase
import com.github.zuev98.currencyconverter.workers.notification.NotificationHelper
import com.github.zuev98.currencyconverter.presentation.util.APP_PREFERENCES
import com.github.zuev98.currencyconverter.presentation.util.APP_PREFERENCES_POSITION
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class CurrencyUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val getLastUpdateUseCase: GetLastUpdateUseCase,
    private val getCurrenciesUseCase: GetCurrenciesUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            var lastUpdate = ""
            val sharedP =
                applicationContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            val position = sharedP.getInt(APP_PREFERENCES_POSITION, -1)
            if (position != -1) {
                lastUpdate = getLastUpdateUseCase.getLastUpdate()
            }

            val currencies = getCurrenciesUseCase.getCurrencies(true)

            if (position != -1) {
                if (lastUpdate != currencies[0].lastUpdate) {
                    val currency = currencies[position].name
                    val value = currencies[position].value
                    sendNotification("$currency: $value")
                } else {
                    sendNotification(NO_UPD_INFO)
                }
            } else {
                sendNotification(DATA_UPLOADED)
            }

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error update database", e)
            Result.failure()
        }
    }

    private fun sendNotification(message: String) {
        NotificationHelper(applicationContext)
            .createNotification(TITLE, message)
    }

    companion object {
        private const val TAG = "Worker"
        private const val TITLE = "Обновление курса валют"
        private const val NO_UPD_INFO = "Нет изменений"
        private const val DATA_UPLOADED = "Загружен курс валют"
    }
}