package com.github.zuev98.currencyconverter.di

import com.github.zuev98.currencyconverter.data.repositories.LocalRepositoryImpl
import com.github.zuev98.currencyconverter.data.repositories.RemoteRepositoryImpl
import com.github.zuev98.currencyconverter.domain.repositories.LocalRepository
import com.github.zuev98.currencyconverter.domain.repositories.RemoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRemoteRepository(
        remoteRepositoryImpl: RemoteRepositoryImpl
    ): RemoteRepository

    @Binds
    @Singleton
    abstract fun bindLocalRepository(
        localRepositoryImpl: LocalRepositoryImpl
    ): LocalRepository
}