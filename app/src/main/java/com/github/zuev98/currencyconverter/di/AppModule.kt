package com.github.zuev98.currencyconverter.di

import android.content.Context
import com.github.zuev98.currencyconverter.data.api.CurrencyApiService
import com.github.zuev98.currencyconverter.data.dao.CurrencyDao
import com.github.zuev98.currencyconverter.data.db.CurrencyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import javax.inject.Singleton

private const val BASE_URL = "https://www.cbr-xml-daily.ru/"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCurrencyApiService(): CurrencyApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideCurrencyDatabase(@ApplicationContext context: Context): CurrencyDatabase {
        return CurrencyDatabase.getInstance(context)
    }

    @Provides
    fun provideCurrencyDao(currencyDatabase: CurrencyDatabase): CurrencyDao {
        return currencyDatabase.getCurrencyDao()
    }
}