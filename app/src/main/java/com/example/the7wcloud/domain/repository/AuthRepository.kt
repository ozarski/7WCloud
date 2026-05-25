package com.example.the7wcloud.domain.repository

import com.example.the7wcloud.domain.model.AuthState
import com.example.the7wcloud.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: UserModel?
    fun observeAuthState(): Flow<AuthState>
    suspend fun signInWithGoogle(idToken: String): Result<UserModel>
    suspend fun signOut()
}
