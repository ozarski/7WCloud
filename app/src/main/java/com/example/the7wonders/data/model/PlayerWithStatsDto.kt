package com.example.the7wonders.data.model

import com.example.the7wonders.domain.model.PlayerModel

data class PlayerWithStatsDto(
    val id: Long,
    val name: String,
    val avgPlacement: Double?,
    val wins: Int?,
    val games: Int?,
    val topScore: Int?
)

fun PlayerWithStatsDto.toDomainModel() = PlayerModel(
    id = id,
    name = name,
    avgPlacement = avgPlacement,
    wins = wins,
    games = games,
    topScore = topScore
)
