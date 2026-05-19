package com.example.the7wonders.data.repository

import com.example.the7wonders.data.model.PlayerDto
import com.example.the7wonders.data.model.PlayerResultDto
import com.example.the7wonders.data.model.toDomainModel
import com.example.the7wonders.domain.model.AddPlayerToGameModel
import com.example.the7wonders.domain.model.PlayerModel
import com.example.the7wonders.domain.model.toPlayerDto
import com.example.the7wonders.domain.repository.PlayerRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class PlayerRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : PlayerRepository {

    override suspend fun getPlayersWithStats(): List<PlayerModel> = coroutineScope {
        val playersDeferred = async { supabaseClient.from("Players").select { filter { eq("deleted", false) } }.decodeList<PlayerDto>() }
        val resultsDeferred = async { supabaseClient.from("PlayerResults").select().decodeList<PlayerResultDto>() }

        val players = playersDeferred.await()
        val results = resultsDeferred.await()

        val statsByPlayer = results.groupBy { it.playerID }
        players.map { player ->
            val playerResults = statsByPlayer[player.id]
            PlayerModel(
                id = player.id,
                name = player.name,
                wins = playerResults?.count { it.placement == 1 },
                games = playerResults?.map { it.gameID }?.distinct()?.size,
                topScore = playerResults?.maxOfOrNull { it.totalPoints },
                avgPlacement = playerResults?.map { it.placement }?.average()
            )
        }
    }

    override suspend fun getAllPlayers(): List<AddPlayerToGameModel> {
        return supabaseClient.from("Players").select { filter { eq("deleted", false) } }.decodeList<PlayerDto>()
            .map { it.toDomainModel() }
            .sortedBy { it.name }
    }

    override suspend fun playerExists(name: String): Boolean {
        val result = supabaseClient.from("Players").select {
            filter {
                eq("name", name)
                eq("deleted", false)
            }
        }.decodeList<PlayerDto>()
        return result.isNotEmpty()
    }

    override suspend fun addPlayer(player: PlayerModel): Long {
        val created = supabaseClient.from("Players").insert<PlayerDto>(player.toPlayerDto()) {
            select()
        }.decodeSingle<PlayerDto>()
        return created.id ?: throw Exception("Failed to get inserted player ID")
    }

    override suspend fun deletePlayer(player: PlayerModel) {
        val id = player.id ?: return
        supabaseClient.from("Players").update(
            { set("deleted", true) }
        ) {
            filter { eq("id", id) }
        }
    }

    override suspend fun updatePlayer(player: PlayerModel) {
        val id = player.id ?: return
        supabaseClient.from("Players").update<PlayerDto>(player.toPlayerDto()) {
            filter { eq("id", id) }
        }
    }
}
