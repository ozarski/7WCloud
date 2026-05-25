package com.example.the7wcloud.domain.repository

import com.example.the7wcloud.domain.model.AddPlayerToGameModel
import com.example.the7wcloud.domain.model.PlayerModel

interface PlayerRepository {

    suspend fun getPlayersWithStats(): List<PlayerModel>

    suspend fun getAllPlayers(): List<AddPlayerToGameModel>

    suspend fun addPlayer(player: PlayerModel): Long

    suspend fun playerExists(name: String): Boolean

    suspend fun deletePlayer(player: PlayerModel)

    suspend fun updatePlayer(player: PlayerModel)
}