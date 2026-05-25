package com.example.the7wcloud.domain.model

import com.example.the7wcloud.data.model.PlayerResultDto

data class PlayerResultModel(
    val playerID: Long,
    val playerName: String,
    val totalScore: Int,
    val placement: Int,
    val scores: List<Pair<PointTypeInterface, Int?>>
)

fun PlayerResultModel.toPlayerResultDto(gameID: Long): PlayerResultDto {
    return PlayerResultDto(
        playerID = playerID,
        gameID = gameID,
        wonderPoints = scores.firstOrNull { it.first == BasePointTypes.Wonder }?.second ?: 0,
        militaryPoints = scores.firstOrNull { it.first == BasePointTypes.Military }?.second ?: 0,
        moneyPoints = scores.firstOrNull { it.first == BasePointTypes.Gold }?.second ?: 0,
        blueCardsPoints = scores.firstOrNull { it.first == BasePointTypes.Blue }?.second ?: 0,
        yellowCardsPoints = scores.firstOrNull { it.first == BasePointTypes.Yellow }?.second ?: 0,
        greenCardsPoints = scores.firstOrNull { it.first == BasePointTypes.Green }?.second ?: 0,
        purpleCardsPoints = scores.firstOrNull { it.first == BasePointTypes.Purple }?.second ?: 0,
        cityCardsPoints = scores.firstOrNull { it.first == CityPointTypes.CityCards }?.second,
        leaderCardsPoints = scores.firstOrNull { it.first == LeaderPointTypes.LeaderCards }?.second,
        navalConflictsPoints = scores.firstOrNull { it.first == ArmadaPointTypes.NavalConflicts }?.second,
        islandCardsPoints = scores.firstOrNull { it.first == ArmadaPointTypes.IslandCards }?.second,
        navalVictoryPoints = scores.firstOrNull { it.first == ArmadaPointTypes.NavalVictory }?.second,
        totalPoints = totalScore,
        placement = placement
    )
}