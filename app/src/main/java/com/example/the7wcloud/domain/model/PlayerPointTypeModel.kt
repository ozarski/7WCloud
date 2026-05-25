package com.example.the7wcloud.domain.model

data class PlayerPointTypeModel(
    val playerID: Long,
    val playerName: String,
    val pointType: PointTypeInterface,
    val value: String
)