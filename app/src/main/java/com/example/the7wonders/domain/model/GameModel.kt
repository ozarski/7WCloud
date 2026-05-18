package com.example.the7wonders.domain.model

import com.example.the7wonders.data.model.GameDto

data class GameModel(
    val id: Long?,
    val date: Long,
    val playerScores: List<Pair<String, Int>>
)

fun GameModel.toGameDto() = GameDto(
    id = id,
    date = date
)