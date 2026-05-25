package com.example.the7wcloud.data.repository

import android.util.Log
import com.example.the7wcloud.domain.model.AuthState
import com.example.the7wcloud.domain.model.UserModel
import com.example.the7wcloud.domain.repository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : AuthRepository {

    companion object {
        private const val TAG = "AuthRepository"
    }

    override val currentUser: UserModel?
        get() {
            val user = supabaseClient.auth.currentUserOrNull()
            Log.d(TAG, "currentUser: ${user?.id ?: "null"}")
            return user?.toDomainModel()
        }

    override fun observeAuthState(): Flow<AuthState> = supabaseClient.auth.sessionStatus
        .map { status ->
            Log.d(TAG, "Session status: ${status::class.simpleName}")
            when (status) {
                is SessionStatus.Authenticated -> {
                    Log.d(TAG, "Authenticated: userId=${status.session.user?.id}")
                    status.session.user?.toDomainModel()
                        ?.let { AuthState.Authenticated(it) }
                        ?: AuthState.Unauthenticated.also {
                            Log.w(TAG, "Authenticated session but user is null")
                        }
                }
                is SessionStatus.Initializing -> {
                    Log.d(TAG, "Session initializing")
                    AuthState.Loading
                }
                is SessionStatus.NotAuthenticated -> {
                    Log.d(TAG, "Not authenticated")
                    AuthState.Unauthenticated
                }
                is SessionStatus.RefreshFailure -> {
                    Log.w(TAG, "Session refresh failed")
                    AuthState.Unauthenticated
                }
            }
        }

    override suspend fun signInWithGoogle(idToken: String): Result<UserModel> = runCatching {
        Log.d(TAG, "signInWithGoogle: idToken length=${idToken.length}")
        require(idToken.isNotBlank()) { "idToken must not be empty" }
        supabaseClient.auth.signInWith(IDToken) {
            this.idToken = idToken
            provider = Google
        }
        val user = supabaseClient.auth.currentUserOrNull()
        if (user == null) {
            Log.e(TAG, "signInWithGoogle: signIn succeeded but currentUser is null")
            throw Exception("Sign in failed")
        }
        Log.i(TAG, "signInWithGoogle succeeded: userId=${user.id}")
        user.toDomainModel()
    }.onFailure { e ->
        Log.e(TAG, "signInWithGoogle failed", e)
    }

    override suspend fun signOut() {
        Log.d(TAG, "signOut")
        try {
            supabaseClient.auth.signOut()
            Log.i(TAG, "signOut succeeded")
        } catch (e: Exception) {
            Log.e(TAG, "signOut failed", e)
        }
    }

    private fun UserInfo.toDomainModel() = UserModel(
        id = id,
        email = email,
        displayName = userMetadata?.get("full_name")?.toString(),
        photoUrl = userMetadata?.get("avatar_url")?.toString()
    ).also {
        Log.d(TAG, "UserInfo -> UserModel: id=${it.id}, email=${it.email}, displayName=${it.displayName}")
    }
}
