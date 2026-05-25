package com.example.the7wcloud.ui.gameDetailsScreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.the7wcloud.domain.model.GameDetailsModel
import com.example.the7wcloud.domain.model.GameModel
import com.example.the7wcloud.domain.repository.AuthRepository
import com.example.the7wcloud.domain.repository.GameRepository
import com.example.the7wcloud.ui.Screens
import com.example.the7wcloud.ui.util.mapToUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameDetailsViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state =
        mutableStateOf(GameDetailsState(gameDetails = GameDetailsModel(playerScores = emptyList())))
    val state: State<GameDetailsState> = _state

    init {
        savedStateHandle.get<String>(Screens.GAME_DETAILS_ID_PARAM)?.let { id ->
            _state.value = _state.value.copy(isLoading = true)
            viewModelScope.launch {
                try {
                    val gameDetails = gameRepository.getGameDetails(id.toLong())
                    val isAdmin = gameRepository.isAdmin()
                    val isOwner = authRepository.currentUser?.id == gameDetails.userId
                    _state.value = _state.value.copy(isLoading = false, gameDetails = gameDetails, canTogglePrivacy = isAdmin || isOwner)
                } catch (e: Exception) {
                    println(e.message)
                    _state.value = _state.value.copy(isLoading = false, error = mapToUserMessage(e))
                }
            }
        }
    }

    fun toggleGamePrivacy() {
        System.err.println("7WCLOUD_TOGGLE_PRIVACY: called, isPrivate=${_state.value.gameDetails.isPrivate}")
        _state.value = _state.value.copy(toggleError = null)
        viewModelScope.launch {
            try {
                val current = _state.value.gameDetails
                val newIsPrivate = !current.isPrivate
                _state.value = _state.value.copy(
                    gameDetails = current.copy(isPrivate = newIsPrivate)
                )
                gameRepository.updateGame(
                    GameModel(
                        id = current.id,
                        date = current.date ?: 0L,
                        playerScores = emptyList(),
                        isPrivate = newIsPrivate
                    )
                )
            } catch (e: Exception) {
                Log.e("GameDetailsVM", "toggleGamePrivacy failed", e)
                System.err.println("7WCLOUD_TOGGLE_PRIVACY_ERROR: ${e::class.simpleName} message=${e.message}")
                val reverted = _state.value.gameDetails.copy(isPrivate = !_state.value.gameDetails.isPrivate)
                _state.value = _state.value.copy(gameDetails = reverted, toggleError = mapToUserMessage(e))
            }
        }
    }

    fun clearToggleError() {
        _state.value = _state.value.copy(toggleError = null)
    }
}