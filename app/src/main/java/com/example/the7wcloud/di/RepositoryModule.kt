package com.example.the7wcloud.di

import com.example.the7wcloud.data.repository.AdminRepositoryImpl
import com.example.the7wcloud.data.repository.AuthRepositoryImpl
import com.example.the7wcloud.data.repository.GameRepositoryImpl
import com.example.the7wcloud.data.repository.PlayerRepositoryImpl
import com.example.the7wcloud.data.repository.PlayerResultRepositoryImpl
import com.example.the7wcloud.domain.repository.AdminRepository
import com.example.the7wcloud.domain.repository.AuthRepository
import com.example.the7wcloud.domain.repository.GameRepository
import com.example.the7wcloud.domain.repository.PlayerRepository
import com.example.the7wcloud.domain.repository.PlayerResultRepository
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

    @Binds
    @Singleton
    abstract fun provideAdminRepository(adminRepositoryImpl: AdminRepositoryImpl): AdminRepository
}