package com.example.the7wonders.ui.adminScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.the7wonders.domain.repository.AdminRepository
import com.example.the7wonders.domain.repository.AuthRepository
import com.example.the7wonders.ui.util.mapToUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminPanelViewModel @Inject constructor(
    private val adminRepository: AdminRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = mutableStateOf(
        AdminPanelState(currentUserId = authRepository.currentUser?.id)
    )
    val state: State<AdminPanelState> = _state

    init {
        loadUsers()
    }

    fun loadUsers() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val users = adminRepository.getAllUsers()
                _state.value = _state.value.copy(isLoading = false, users = users)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = mapToUserMessage(e)
                )
            }
        }
    }

    fun setUserRole(userId: String, newRole: String) {
        _state.value = _state.value.copy(actionLoadingUserId = userId, actionError = null)
        viewModelScope.launch {
            try {
                adminRepository.setUserRole(userId, newRole)
                _state.value = _state.value.copy(actionLoadingUserId = null)
                loadUsers()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    actionLoadingUserId = null,
                    actionError = mapToUserMessage(e)
                )
            }
        }
    }

    fun clearActionError() {
        _state.value = _state.value.copy(actionError = null)
    }

    fun confirmDeletePlayers(userId: String) {
        _state.value = _state.value.copy(showDeletePlayersConfirmation = userId)
    }

    fun dismissDeletePlayersConfirmation() {
        _state.value = _state.value.copy(showDeletePlayersConfirmation = null)
    }

    fun deleteUserPlayers(userId: String) {
        _state.value = _state.value.copy(
            showDeletePlayersConfirmation = null,
            deletePlayersLoadingUserId = userId,
            actionError = null
        )
        viewModelScope.launch {
            try {
                adminRepository.deleteUserPlayers(userId)
                _state.value = _state.value.copy(
                    deletePlayersLoadingUserId = null,
                    playersDeleted = true
                )
                loadUsers()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    deletePlayersLoadingUserId = null,
                    actionError = mapToUserMessage(e)
                )
            }
        }
    }

    fun confirmDeleteGames(userId: String) {
        _state.value = _state.value.copy(showDeleteGamesConfirmation = userId)
    }

    fun dismissDeleteGamesConfirmation() {
        _state.value = _state.value.copy(showDeleteGamesConfirmation = null)
    }

    fun confirmDeleteAccount(userId: String) {
        _state.value = _state.value.copy(showDeleteAccountConfirmation = userId)
    }

    fun dismissDeleteAccountConfirmation() {
        _state.value = _state.value.copy(showDeleteAccountConfirmation = null)
    }

    fun deleteUserGames(userId: String) {
        _state.value = _state.value.copy(
            showDeleteGamesConfirmation = null,
            deleteGamesLoadingUserId = userId,
            actionError = null
        )
        viewModelScope.launch {
            try {
                adminRepository.deleteUserGames(userId)
                _state.value = _state.value.copy(
                    deleteGamesLoadingUserId = null,
                    gamesDeleted = true
                )
                loadUsers()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    deleteGamesLoadingUserId = null,
                    actionError = mapToUserMessage(e)
                )
            }
        }
    }

    fun deleteUserAccount(userId: String) {
        _state.value = _state.value.copy(
            showDeleteAccountConfirmation = null,
            deleteAccountLoadingUserId = userId,
            actionError = null
        )
        viewModelScope.launch {
            try {
                adminRepository.deleteUserAccount(userId)
                _state.value = _state.value.copy(
                    deleteAccountLoadingUserId = null,
                    gamesDeleted = true
                )
                loadUsers()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    deleteAccountLoadingUserId = null,
                    actionError = mapToUserMessage(e)
                )
            }
        }
    }
}
