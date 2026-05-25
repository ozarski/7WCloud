package com.example.the7wcloud.data.model

import com.example.the7wcloud.domain.model.AddPlayerToGameModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayerDto(
    val id: Long? = null,
    @SerialName("created_at") val createdAt: String? = null,
    val name: String,
    @SerialName("isPrivate") val isPrivate: Boolean = false,
    @SerialName("userId") val userId: String? = null,
    val deleted: Boolean = false
)

fun PlayerDto.toDomainModel(): AddPlayerToGameModel {
    if (id == null) throw Exception("Player ID cannot be null")
    return AddPlayerToGameModel(
        id = id,
        name = name,
        isPlaying = false,
        ordinal = null
    )
}
