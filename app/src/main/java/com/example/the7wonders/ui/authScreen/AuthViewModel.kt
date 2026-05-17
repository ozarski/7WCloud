package com.example.the7wonders.ui.authScreen

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

    private val _state = mutableStateOf(AuthUiState())
    val state: State<AuthUiState> = _state

    init {
        viewModelScope.launch {
            authRepository.observeAuthState().collect { authState ->
                _state.value = _state.value.copy(authState = authState)
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            authRepository.signInWithGoogle(idToken)
                .onFailure { e ->
                    _state.value = _state.value.copy(isLoading = false, error = e.message)
                }
                .onSuccess {
                    _state.value = _state.value.copy(isLoading = false, error = null)
                }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
