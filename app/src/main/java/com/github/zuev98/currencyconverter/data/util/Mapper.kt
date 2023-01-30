package com.github.zuev98.currencyconverter.data.util

import com.github.zuev98.currencyconverter.data.entity.Currency
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

fun String.toCurrency(): List<Currency> {
    val currenciesRawList =
        JSONObject(this)
            .getJSONObject("Valute")
            .toString()
            .split(Regex("\"[A-Z]{3}\":"))

    val lastUpdate = getFormattedDateString(JSONObject(this).getString("Date"))
    val currencies = mutableListOf<Currency>()

    for (i in 1..currenciesRawList.lastIndex) {
        val currencyJsonObj = JSONObject(currenciesRawList[i])
        currencies.add(
            Currency(
                id = currencyJsonObj.getString("ID"),
                charCode = currencyJsonObj.getString("CharCode"),
                nominal = currencyJsonObj.getInt("Nominal"),
                name = currencyJsonObj.getString("Name"),
                value = currencyJsonObj.getDouble("Value"),
                lastUpdate = lastUpdate
            )
        )
    }

    return currencies
}

private fun getFormattedDateString(lastUpdate: String): String {
    val parseFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("Ru"))
    val dateFormat =
        SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("Ru"))

    return dateFormat.format(parseFormat.parse(lastUpdate) as Date)
}