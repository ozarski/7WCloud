package com.example.the7wonders.data.model

import com.example.the7wonders.domain.model.AdminUserModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdminUserDto(
    val id: String,
    val email: String? = null,
    @SerialName("display_name")
    val displayName: String? = null,
    @SerialName("photo_url")
    val photoUrl: String? = null,
    val role: String? = null
) {
    fun toDomainModel() = AdminUserModel(
        id = id,
        email = email,
        displayName = displayName,
        photoUrl = photoUrl,
        role = role ?: "user"
    )
}
