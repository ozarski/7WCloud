package com.example.the7wonders.domain.model

data class UserModel(
    val id: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?
)
