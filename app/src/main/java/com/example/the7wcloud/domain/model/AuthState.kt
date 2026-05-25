package com.example.the7wcloud.domain.model

sealed interface AuthState {
    data object Loading : AuthState
    data object Unauthenticated : AuthState
    data class Authenticated(val user: UserModel) : AuthState
}
