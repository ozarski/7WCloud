package com.example.the7wcloud.domain.repository

import com.example.the7wcloud.domain.model.PlayerResultModel

interface PlayerResultRepository {

    suspend fun addPlayerResult(playerResultModel: PlayerResultModel, gameID: Long)

    suspend fun deletePlayerResult(playerResultModel: PlayerResultModel, gameID: Long)

    suspend fun deletePlayerResultsForGame(gameID: Long)
}