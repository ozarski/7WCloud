package com.example.the7wcloud.domain.repository

import com.example.the7wcloud.domain.model.AdminUserModel

interface AdminRepository {
    suspend fun getAllUsers(): List<AdminUserModel>
    suspend fun setUserRole(userId: String, role: String)
    suspend fun deleteUserPlayers(userId: String)
    suspend fun deleteUserGames(userId: String)
    suspend fun deleteUserAccount(userId: String)
}
