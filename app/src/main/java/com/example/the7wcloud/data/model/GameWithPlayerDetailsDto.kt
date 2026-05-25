package com.example.the7wcloud.data.model

import com.example.the7wcloud.domain.model.ArmadaPointTypes
import com.example.the7wcloud.domain.model.BasePointTypes
import com.example.the7wcloud.domain.model.CityPointTypes
import com.example.the7wcloud.domain.model.GameDetailsModel
import com.example.the7wcloud.domain.model.LeaderPointTypes
import com.example.the7wcloud.domain.model.PlayerResultModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameWithPlayerDetailsDto(
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
    val date: Long,
    val name: String,
) {
    companion object {
        fun toGameDetailsModel(scores: List<GameWithPlayerDetailsDto>, isPrivate: Boolean = false, userId: String? = null): GameDetailsModel {
            if (scores.isEmpty()) {
                throw Exception("Score list is empty!")
            }
            return GameDetailsModel(
                id = scores.first().gameID,
                date = scores.first().date,
                isPrivate = isPrivate,
                userId = userId,
                playerScores = scores.map { it.toPlayerResultModel() }
            )
        }
    }
}

fun GameWithPlayerDetailsDto.toPlayerResultModel() = PlayerResultModel(
    playerID = playerID,
    playerName = name,
    totalScore = totalPoints,
    placement = placement,
    scores = listOf(
        Pair(BasePointTypes.Wonder, wonderPoints),
        Pair(BasePointTypes.Military, militaryPoints),
        Pair(BasePointTypes.Gold, moneyPoints),
        Pair(BasePointTypes.Blue, blueCardsPoints),
        Pair(BasePointTypes.Yellow, yellowCardsPoints),
        Pair(BasePointTypes.Green, greenCardsPoints),
        Pair(BasePointTypes.Purple, purpleCardsPoints),
        Pair(CityPointTypes.CityCards, cityCardsPoints),
        Pair(LeaderPointTypes.LeaderCards, leaderCardsPoints),
        Pair(ArmadaPointTypes.NavalConflicts, navalConflictsPoints),
        Pair(ArmadaPointTypes.IslandCards, islandCardsPoints),
        Pair(ArmadaPointTypes.NavalVictory, navalVictoryPoints)
    )
)
