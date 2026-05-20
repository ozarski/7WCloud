package com.example.the7wonders.data.repository

import com.example.the7wonders.data.model.PlayerResultDto
import com.example.the7wonders.domain.model.PlayerResultModel
import com.example.the7wonders.domain.model.toPlayerResultDto
import com.example.the7wonders.domain.repository.PlayerResultRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import jakarta.inject.Inject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class PlayerResultRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : PlayerResultRepository {

    override suspend fun addPlayerResult(playerResultModel: PlayerResultModel, gameID: Long) {
        supabaseClient.from("PlayerResults").insert<PlayerResultDto>(playerResultModel.toPlayerResultDto(gameID))
    }

    override suspend fun deletePlayerResult(playerResultModel: PlayerResultModel, gameID: Long) {
        supabaseClient.from("PlayerResults").delete {
            filter {
                eq("playerID", playerResultModel.playerID)
                eq("gameID", gameID)
            }
        }
    }

    override suspend fun deletePlayerResultsForGame(gameID: Long) {
        supabaseClient.postgrest.rpc("delete_player_results_for_game", buildJsonObject {
            put("game_id", gameID)
        })
    }
}
