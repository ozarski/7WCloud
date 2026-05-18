package com.example.the7wonders.data.repository

import com.example.the7wonders.data.model.GameDto
import com.example.the7wonders.data.model.GameWithPlayerDetailsDto
import com.example.the7wonders.data.model.PlayerDto
import com.example.the7wonders.data.model.PlayerResultDto
import com.example.the7wonders.domain.model.GameDetailsModel
import com.example.the7wonders.domain.model.GameModel
import com.example.the7wonders.domain.repository.GameRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

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
        }
        GameWithPlayerDetailsDto.toGameDetailsModel(scores)
    }

    override suspend fun addGame(game: GameModel): Long {
        val created = supabaseClient.from("Games").insert(game.toGameDto()) {
            select()
        }.decodeSingle<GameDto>()
        return created.id ?: throw Exception("Failed to get inserted game ID")
    }

    override suspend fun deleteGame(game: GameModel) {
        supabaseClient.from("Games").delete {
            filter { eq("id", game.id) }
        }
    }
}
