package com.example.the7wonders.domain.model

data class AdminUserModel(
    val id: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?,
    val role: String
)
