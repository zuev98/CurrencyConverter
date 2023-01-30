package com.github.zuev98.currencyconverter.domain.usecases

import com.github.zuev98.currencyconverter.domain.repositories.LocalRepository
import javax.inject.Inject

class GetLastUpdateUseCase @Inject constructor(
    private val repository: LocalRepository
) {

    suspend fun getLastUpdate(): String {
        return repository.getLastUpdate()
    }
}