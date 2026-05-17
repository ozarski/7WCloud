package com.example.the7wonders.data.repository

import com.example.the7wonders.domain.model.AuthState
import com.example.the7wonders.domain.model.UserModel
import com.example.the7wonders.domain.repository.AuthRepository
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

    override val currentUser: UserModel?
        get() = supabaseClient.auth.currentUserOrNull()?.toDomainModel()

    override fun observeAuthState(): Flow<AuthState> = supabaseClient.auth.sessionStatus
        .map { status ->
            when (status) {
                is SessionStatus.Authenticated -> {
                    status.session.user?.toDomainModel()
                        ?.let { AuthState.Authenticated(it) }
                        ?: AuthState.Unauthenticated
                }
                is SessionStatus.Initializing -> AuthState.Loading
                is SessionStatus.NotAuthenticated -> AuthState.Unauthenticated
                is SessionStatus.RefreshFailure -> AuthState.Unauthenticated
            }
        }

    override suspend fun signInWithGoogle(idToken: String): Result<UserModel> = runCatching {
        supabaseClient.auth.signInWith(IDToken) {
            this.idToken = idToken
            provider = Google
        }
        supabaseClient.auth.currentUserOrNull()?.toDomainModel()
            ?: throw Exception("Sign in failed")
    }

    override suspend fun signOut() {
        supabaseClient.auth.signOut()
    }

    private fun UserInfo.toDomainModel() = UserModel(
        id = id,
        email = email,
        displayName = userMetadata?.get("full_name")?.toString(),
        photoUrl = userMetadata?.get("avatar_url")?.toString()
    )
}
