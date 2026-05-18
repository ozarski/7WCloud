package com.example.the7wonders.domain.repository

import com.example.the7wonders.domain.model.GameDetailsModel
import com.example.the7wonders.domain.model.GameModel

interface GameRepository {

    suspend fun getGames(): List<GameModel>

    suspend fun getGameDetails(id: Long): GameDetailsModel

    suspend fun addGame(game: GameModel): Long

    suspend fun deleteGame(game: GameModel)
}