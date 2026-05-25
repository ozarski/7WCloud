package com.example.the7wcloud.data.model

import com.example.the7wcloud.domain.model.PlayerModel

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
