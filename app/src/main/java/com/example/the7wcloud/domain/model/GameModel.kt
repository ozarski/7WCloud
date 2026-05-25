package com.example.the7wcloud.domain.model

import com.example.the7wcloud.data.model.GameDto

data class GameModel(
    val id: Long?,
    val date: Long,
    val playerScores: List<Pair<String, Int>>,
    val isPrivate: Boolean = false
)

fun GameModel.toGameDto() = GameDto(
    id = id,
    date = date,
    isPrivate = isPrivate
)