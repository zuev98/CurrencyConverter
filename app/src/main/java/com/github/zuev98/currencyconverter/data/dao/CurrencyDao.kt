package com.github.zuev98.currencyconverter.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.zuev98.currencyconverter.data.entity.Currency

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM Currency")
    suspend fun getCurrencies(): List<Currency>

    @Query("SELECT lastUpdate FROM Currency LIMIT 1")
    suspend fun getLastUpdate(): String

    @Query("SELECT id FROM Currency WHERE id = :id LIMIT 1")
    suspend fun getId(id: String): String?

    @Query("UPDATE currency SET value = :value, lastUpdate = :lastUpdate WHERE id = :id")
    suspend fun updateCurrency(id: String, value: Double, lastUpdate: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCurrency(currency: Currency)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(currencies: List<Currency>)

    suspend fun updateOrInsert(currency: Currency) {
        val entityId = getId(currency.id)
        if (entityId == currency.id) {
            updateCurrency(currency.id, currency.value, currency.lastUpdate)
        } else {
            addCurrency(currency)
        }
    }
}