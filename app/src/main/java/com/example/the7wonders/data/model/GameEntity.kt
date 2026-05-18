package com.example.the7wonders.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameDto(
    val id: Long? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("isPrivate") val isPrivate: Boolean = false,
    @SerialName("userId") val userId: String? = null,
    val date: Long
)
