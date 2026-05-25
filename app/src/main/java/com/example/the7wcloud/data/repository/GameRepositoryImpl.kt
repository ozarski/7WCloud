package com.example.the7wcloud.data.repository

import android.util.Log
import com.example.the7wcloud.data.model.GameDto
import com.example.the7wcloud.data.model.GameWithPlayerDetailsDto
import com.example.the7wcloud.data.model.PlayerDto
import com.example.the7wcloud.data.model.PlayerResultDto
import com.example.the7wcloud.domain.model.GameDetailsModel
import com.example.the7wcloud.domain.model.GameModel
import com.example.the7wcloud.domain.model.toGameDto
import com.example.the7wcloud.domain.repository.GameRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class GameRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : GameRepository {

    override suspend fun getGames(): List<GameModel> = coroutineScope {
        val gamesDeferred = async { supabaseClient.from("Games").select().decodeList<GameDto>() }
        val resultsDeferred = async { supabaseClient.from("PlayerResults").select().decodeList<PlayerResultDto>() }
        val playersDeferred = async { supabaseClient.from("Players").select().decodeList<PlayerDto>() }

        val games = gamesDeferred.await()
        val results = resultsDeferred.await()
        val players = playersDeferred.await()

        val gameDates = games.associate { it.id!! to it.date }
        val playerNames = players.associate { it.id!! to it.name }

        results.groupBy { it.gameID }
            .map { (gameId, rows) ->
                GameModel(
                    id = gameId,
                    date = gameDates[gameId] ?: 0L,
                    playerScores = rows.map { result ->
                        Pair(playerNames[result.playerID] ?: "Unknown", result.totalPoints)
                    }
                )
            }
            .sortedByDescending { it.date }
    }

    override suspend fun getGameDetails(id: Long): GameDetailsModel = coroutineScope {
        val resultsDeferred = async {
            supabaseClient.from("PlayerResults").select {
                filter { eq("gameID", id) }
            }.decodeList<PlayerResultDto>()
        }
        val gameDeferred = async {
            supabaseClient.from("Games").select {
                filter { eq("id", id) }
            }.decodeSingle<GameDto>()
        }
        val playersDeferred = async {
            supabaseClient.from("Players").select().decodeList<PlayerDto>()
        }

        val results = resultsDeferred.await()
        val game = gameDeferred.await()
        val players = playersDeferred.await()

        if (results.isEmpty()) throw Exception("Score list is empty!")

        val playerNames = players.associate { it.id!! to it.name }

        val scores = results.map { result ->
            GameWithPlayerDetailsDto(
                playerID = result.playerID,
                gameID = result.gameID,
                wonderPoints = result.wonderPoints,
                militaryPoints = result.militaryPoints,
                moneyPoints = result.moneyPoints,
                blueCardsPoints = result.blueCardsPoints,
                yellowCardsPoints = result.yellowCardsPoints,
                greenCardsPoints = result.greenCardsPoints,
                purpleCardsPoints = result.purpleCardsPoints,
                cityCardsPoints = result.cityCardsPoints,
                leaderCardsPoints = result.leaderCardsPoints,
                navalConflictsPoints = result.navalConflictsPoints,
                islandCardsPoints = result.islandCardsPoints,
                navalVictoryPoints = result.navalVictoryPoints,
                totalPoints = result.totalPoints,
                placement = result.placement,
                date = game.date,
                name = playerNames[result.playerID] ?: "Unknown"
            )
        }.sortedBy { it.placement }
        GameWithPlayerDetailsDto.toGameDetailsModel(scores, game.isPrivate, game.userId)
    }

    override suspend fun addGame(game: GameModel): Long {
        val created = supabaseClient.from("Games").insert<GameDto>(game.toGameDto()) {
            select()
        }.decodeSingle<GameDto>()
        return created.id ?: throw Exception("Failed to get inserted game ID")
    }

    override suspend fun deleteGame(game: GameModel) {
        val id = game.id ?: return
        supabaseClient.postgrest.rpc("delete_game", buildJsonObject {
            put("game_id", id)
        })
    }

    override suspend fun updateGame(game: GameModel) {
        val id = game.id ?: return
        supabaseClient.postgrest.rpc("update_game_privacy", buildJsonObject {
            put("game_id", id)
            put("is_private", game.isPrivate)
        })
    }

    override suspend fun isAdmin(): Boolean {
        return try {
            supabaseClient.postgrest.rpc("is_admin").decodeAs<Boolean>()
        } catch (e: Exception) {
            false
        }
    }
}
