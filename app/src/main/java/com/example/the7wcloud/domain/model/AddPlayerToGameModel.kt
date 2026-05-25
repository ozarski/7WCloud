package com.example.the7wcloud.domain.model

data class AddPlayerToGameModel(
    val id: Long,
    val name: String,
    val isPlaying: Boolean,
    val ordinal: Int?
)