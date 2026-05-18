package com.example.the7wonders.ui.authScreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.the7wonders.domain.model.AuthState
import com.example.the7wonders.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val authState: AuthState = AuthState.Loading,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    companion object {
        private const val TAG = "AuthViewModel"
    }

    private val _state = mutableStateOf(AuthUiState())
    val state: State<AuthUiState> = _state

    init {
        viewModelScope.launch {
            try {
                authRepository.observeAuthState().collect { authState ->
                    Log.d(TAG, "Auth state changed: ${authState::class.simpleName}")
                    _state.value = _state.value.copy(authState = authState)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Auth state observation failed", e)
                _state.value = _state.value.copy(
                    authState = AuthState.Unauthenticated,
                    error = "Auth observation failed: ${e.message}"
                )
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        Log.d(TAG, "signInWithGoogle called, idToken length=${idToken.length}")
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            authRepository.signInWithGoogle(idToken)
                .onFailure { e ->
                    Log.e(TAG, "signInWithGoogle failed", e)
                    _state.value = _state.value.copy(isLoading = false, error = e.message)
                }
                .onSuccess { user ->
                    Log.i(TAG, "signInWithGoogle succeeded: userId=${user.id}")
                    _state.value = _state.value.copy(isLoading = false, error = null)
                }
        }
    }

    fun signOut() {
        Log.d(TAG, "signOut called")
        viewModelScope.launch {
            try {
                authRepository.signOut()
                Log.i(TAG, "signOut succeeded")
            } catch (e: Exception) {
                Log.e(TAG, "signOut failed", e)
            }
        }
    }

    fun setError(message: String) {
        Log.w(TAG, "Error set from UI: $message")
        _state.value = _state.value.copy(isLoading = false, error = message)
    }

    fun clearError() {
        Log.d(TAG, "clearError")
        _state.value = _state.value.copy(error = null)
    }
}
