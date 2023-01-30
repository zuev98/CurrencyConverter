package com.github.zuev98.currencyconverter.domain.repositories

import com.github.zuev98.currencyconverter.data.entity.Currency

interface RemoteRepository {
    fun getCurrencies(): List<Currency>
}