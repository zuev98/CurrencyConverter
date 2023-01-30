package com.github.zuev98.currencyconverter.domain.repositories

import com.github.zuev98.currencyconverter.data.entity.Currency

interface LocalRepository {
    suspend fun getCurrencies(): List<Currency>

    suspend fun getLastUpdate(): String

    suspend fun updateOrInsert(currency: Currency)

    suspend fun addAll(currencies: List<Currency>)
}