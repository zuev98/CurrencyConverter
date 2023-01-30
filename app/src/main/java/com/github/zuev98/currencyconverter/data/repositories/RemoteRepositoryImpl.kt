package com.github.zuev98.currencyconverter.data.repositories

import android.util.Log
import com.github.zuev98.currencyconverter.data.api.CurrencyApiService
import com.github.zuev98.currencyconverter.data.entity.Currency
import com.github.zuev98.currencyconverter.data.util.toCurrency
import com.github.zuev98.currencyconverter.domain.repositories.RemoteRepository
import java.io.IOException
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(
    private val api: CurrencyApiService
) : RemoteRepository {

    override fun getCurrencies(): List<Currency> {
        val response = try {
            api.getCurrencies().execute()
        } catch (e: IOException) {
            Log.w(TAG, "A network error has occurred, throwing IOException")
            throw IOException("A network error")
        }

        if (!response.isSuccessful) {
            Log.w(TAG, "A response error has occurred, throwing Exception")
            throw Exception("A response error")
        }

        val responseBody = response.body()
        if (responseBody == null) {
            Log.w(TAG, "A unexpected error has occurred, throwing Exception")
            throw Exception("An unexpected error")
        }

        return responseBody.toCurrency()
    }

    companion object {
        private const val TAG = "remoteRepositoryImpl"
    }
}