package com.example.the7wcloud.domain.model

data class AdminUserModel(
    val id: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?,
    val role: String
)
