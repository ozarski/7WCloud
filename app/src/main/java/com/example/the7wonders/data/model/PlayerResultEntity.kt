package com.example.the7wonders.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayerResultDto(
    @SerialName("playerID") val playerID: Long,
    @SerialName("gameID") val gameID: Long,
    @SerialName("wonderPoints") val wonderPoints: Int,
    @SerialName("militaryPoints") val militaryPoints: Int,
    @SerialName("moneyPoints") val moneyPoints: Int = 0,
    @SerialName("blueCardsPoints") val blueCardsPoints: Int = 0,
    @SerialName("yellowCardsPoints") val yellowCardsPoints: Int = 0,
    @SerialName("greenCardsPoints") val greenCardsPoints: Int = 0,
    @SerialName("purpleCardsPoints") val purpleCardsPoints: Int = 0,
    @SerialName("cityCardsPoints") val cityCardsPoints: Int? = null,
    @SerialName("leaderCardsPoints") val leaderCardsPoints: Int? = null,
    @SerialName("navalConflictsPoints") val navalConflictsPoints: Int? = null,
    @SerialName("islandCardsPoints") val islandCardsPoints: Int? = null,
    @SerialName("navalVictoryPoints") val navalVictoryPoints: Int? = null,
    @SerialName("totalPoints") val totalPoints: Int,
    @SerialName("placement") val placement: Int,
    @SerialName("userId") val userId: String? = null
)
