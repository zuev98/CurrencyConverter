package com.github.zuev98.currencyconverter.data.repositories

import com.github.zuev98.currencyconverter.data.dao.CurrencyDao
import com.github.zuev98.currencyconverter.data.entity.Currency
import com.github.zuev98.currencyconverter.domain.repositories.LocalRepository
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val currencyDao: CurrencyDao
) : LocalRepository {

    override suspend fun getCurrencies(): List<Currency> {
        return currencyDao.getCurrencies()
    }

    override suspend fun getLastUpdate(): String {
        return currencyDao.getLastUpdate()
    }

    override suspend fun updateOrInsert(currency: Currency) {
        currencyDao.updateOrInsert(currency)
    }

    override suspend fun addAll(currencies: List<Currency>) {
        currencyDao.addAll(currencies)
    }
}