package com.github.zuev98.currencyconverter.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Currency(
    @PrimaryKey
    val id: String,
    val charCode: String,
    val nominal: Int,
    val name: String,
    val value: Double,
    val lastUpdate: String
)
