package com.example.the7wonders.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameWithResultsDto(
    @SerialName("gameID") val gameID: Long,
    @SerialName("playerID") val playerID: Long,
    val name: String,
    val date: Long,
    @SerialName("totalPoints") val totalScore: Int
)
