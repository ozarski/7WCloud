package com.example.the7wonders.data.repository

import com.example.the7wonders.data.model.AdminUserDto
import com.example.the7wonders.domain.model.AdminUserModel
import com.example.the7wonders.domain.repository.AdminRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import jakarta.inject.Inject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AdminRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : AdminRepository {

    override suspend fun getAllUsers(): List<AdminUserModel> {
        return supabaseClient.postgrest.rpc("list_users")
            .decodeList<AdminUserDto>()
            .map { it.toDomainModel() }
    }

    override suspend fun setUserRole(userId: String, role: String) {
        supabaseClient.postgrest.rpc("set_user_role", buildJsonObject {
            put("target_user_id", userId)
            put("new_role", role)
        })
    }

    override suspend fun deleteUserPlayers(userId: String) {
        supabaseClient.postgrest.rpc("delete_user_players", buildJsonObject {
            put("target_user_id", userId)
        })
    }

    override suspend fun deleteUserGames(userId: String) {
        supabaseClient.postgrest.rpc("delete_user_games", buildJsonObject {
            put("target_user_id", userId)
        })
    }

    override suspend fun deleteUserAccount(userId: String) {
        supabaseClient.postgrest.rpc("delete_user_account", buildJsonObject {
            put("target_user_id", userId)
        })
    }
}
