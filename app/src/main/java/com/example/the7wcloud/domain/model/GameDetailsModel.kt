package com.example.the7wcloud.domain.model

data class GameDetailsModel(
    val id: Long? = null,
    val date: Long? = null,
    val isPrivate: Boolean = false,
    val userId: String? = null,
    val playerScores: List<PlayerResultModel>
)