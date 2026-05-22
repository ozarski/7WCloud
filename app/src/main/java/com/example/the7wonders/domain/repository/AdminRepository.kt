package com.example.the7wonders.domain.repository

import com.example.the7wonders.domain.model.AdminUserModel

interface AdminRepository {
    suspend fun getAllUsers(): List<AdminUserModel>
    suspend fun setUserRole(userId: String, role: String)
}
