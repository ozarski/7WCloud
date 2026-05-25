package com.example.the7wcloud.domain.repository

import com.example.the7wcloud.domain.model.GameDetailsModel
import com.example.the7wcloud.domain.model.GameModel

interface GameRepository {

    suspend fun getGames(): List<GameModel>

    suspend fun getGameDetails(id: Long): GameDetailsModel

    suspend fun addGame(game: GameModel): Long

    suspend fun deleteGame(game: GameModel)

    suspend fun updateGame(game: GameModel)

    suspend fun isAdmin(): Boolean
}