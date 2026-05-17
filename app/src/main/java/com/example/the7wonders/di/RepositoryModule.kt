package com.example.the7wonders.di

import com.example.the7wonders.data.repository.AuthRepositoryImpl
import com.example.the7wonders.data.repository.GameRepositoryImpl
import com.example.the7wonders.data.repository.PlayerRepositoryImpl
import com.example.the7wonders.data.repository.PlayerResultRepositoryImpl
import com.example.the7wonders.domain.repository.AuthRepository
import com.example.the7wonders.domain.repository.GameRepository
import com.example.the7wonders.domain.repository.PlayerRepository
import com.example.the7wonders.domain.repository.PlayerResultRepository
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
    abstract fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun provideGameRepository(gameRepositoryImpl: GameRepositoryImpl): GameRepository

    @Binds
    @Singleton
    abstract fun providePlayerRepository(playerRepositoryImpl: PlayerRepositoryImpl): PlayerRepository

    @Binds
    @Singleton
    abstract fun providePlayerResultRepository(playerResultRepositoryImpl: PlayerResultRepositoryImpl): PlayerResultRepository
}