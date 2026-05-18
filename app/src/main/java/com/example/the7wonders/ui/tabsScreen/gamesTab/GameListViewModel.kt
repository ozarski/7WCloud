package com.example.the7wonders.ui.tabsScreen.gamesTab

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.the7wonders.domain.model.GameModel
import com.example.the7wonders.domain.repository.GameRepository
import com.example.the7wonders.domain.repository.PlayerResultRepository
import com.example.the7wonders.ui.util.mapToUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class GameListViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val playerResultRepository: PlayerResultRepository
) :
    ViewModel() {

    private val _state = mutableStateOf(GameListState(emptyList()))
    val state: State<GameListState> = _state

    init {
        viewModelScope.launch {
            loadGames()
        }
    }

    fun loadGames() {
        _state.value = _state.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val games = gameRepository.getGames()
                _state.value = _state.value.copy(isLoading = false, gameList = games)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = mapToUserMessage(e))
            }
        }
    }

    fun generateMockData(n: Int): List<GameModel> {
        val names = listOf(
            "Wojtek",
            "Szymon",
            "Kamil",
            "Kalina",
            "Kamila",
            "Kasia",
        )
        val games = mutableListOf<GameModel>()
        for (i in 0..n) {
            val scores = (30..70).shuffled().take(6)
            val playerScores = names.mapIndexed { index, name ->
                Pair(name, scores[index])
            }
            games.add(
                GameModel(
                    id = i + 1L,
                    playerScores = playerScores,
                    date = Calendar.getInstance().timeInMillis
                )
            )
        }
        return games
    }

    fun toggleDeletePopup(gameModel: GameModel?) {
        _state.value = _state.value.copy(
            deletePopupVisible = !_state.value.deletePopupVisible,
            popupGameModel = gameModel
        )
    }

    fun deleteGame() {
        val gameModel = _state.value.popupGameModel ?: return
        viewModelScope.launch {
            try {
                if (gameModel.id != null) {
                    playerResultRepository.deletePlayerResultsForGame(gameModel.id)
                }
                gameRepository.deleteGame(gameModel)
                toggleDeletePopup(null)
                loadGames()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = mapToUserMessage(e))
            }
        }
    }
}