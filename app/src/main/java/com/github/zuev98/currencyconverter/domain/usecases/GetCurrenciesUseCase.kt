package com.github.zuev98.currencyconverter.domain.usecases

import com.github.zuev98.currencyconverter.data.entity.Currency
import com.github.zuev98.currencyconverter.domain.repositories.LocalRepository
import com.github.zuev98.currencyconverter.domain.repositories.RemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCurrenciesUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) {

    suspend fun getCurrencies(isRemote: Boolean): List<Currency> {
        return if (isRemote)
            getRemoteCurrencies()
        else
            getLocalCurrencies()
    }

    private suspend fun getRemoteCurrencies(): List<Currency> =
        withContext(Dispatchers.IO) {
            try {
                val remoteCurrencies = remoteRepository.getCurrencies()

                val localCurrencies = localRepository.getCurrencies()
                if (localCurrencies.isEmpty()) {
                    localRepository.addAll(remoteCurrencies)
                }
                else if (localCurrencies[0].lastUpdate != remoteCurrencies[0].lastUpdate) {
                    remoteCurrencies.forEach { currency ->
                        localRepository.updateOrInsert(currency)
                    }
                }

                remoteCurrencies
            } catch (e: IOException) {
                throw e
            } catch (e: Exception) {
                throw e
            }
        }

    private suspend fun getLocalCurrencies(): List<Currency> =
        withContext(Dispatchers.IO) {
            localRepository.getCurrencies()
                .ifEmpty { throw IOException("Empty database") }
        }
}