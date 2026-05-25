package com.example.the7wcloud.domain.model

import com.example.the7wcloud.data.model.PlayerDto

data class PlayerModel(
    val id: Long? = null,
    val name: String,
    val isPrivate: Boolean = false,
    val wins: Int? = null,
    val games: Int? = null,
    val topScore: Int? = null,
    val avgPlacement: Double? = null
)

fun PlayerModel.toPlayerDto() = PlayerDto(
    id = id,
    name = name,
    isPrivate = isPrivate
)