package com.example.the7wcloud.ui.addGameScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.the7wcloud.domain.model.ArmadaPointTypes
import com.example.the7wcloud.domain.model.BasePointTypes
import com.example.the7wcloud.domain.model.CityPointTypes
import com.example.the7wcloud.domain.model.GameModel
import com.example.the7wcloud.domain.model.LeaderPointTypes
import com.example.the7wcloud.domain.model.PlayerPointTypeModel
import com.example.the7wcloud.domain.model.PlayerResultModel
import com.example.the7wcloud.domain.repository.GameRepository
import com.example.the7wcloud.domain.repository.PlayerRepository
import com.example.the7wcloud.domain.repository.PlayerResultRepository
import com.example.the7wcloud.ui.util.mapToUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import java.util.Calendar

@HiltViewModel
class AddGameViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val gameRepository: GameRepository,
    private val playerResultRepository: PlayerResultRepository
) :
    ViewModel() {

    val maxPlayers = 7
    private val _state = mutableStateOf(AddGameState(emptyList()))
    val state: State<AddGameState> = _state

    init {
        loadAvailablePlayers()
    }

    fun loadAvailablePlayers() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val players = playerRepository.getAllPlayers()
                _state.value = _state.value.copy(availablePlayers = players, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = mapToUserMessage(e))
            }
        }
    }

    private fun addPlayer(playerIndex: Int) {
        val ordinal = getNextPlayerNumber()
        if (ordinal > maxPlayers) return
        val modifiedPlayers = _state.value.availablePlayers.toMutableList()
        modifiedPlayers[playerIndex] =
            modifiedPlayers[playerIndex].copy(isPlaying = true, ordinal = ordinal)
        _state.value = _state.value.copy(
            availablePlayers = modifiedPlayers
        )
    }

    private fun removePlayer(playerIndex: Int) {
        var modifiedPlayers = _state.value.availablePlayers.toMutableList()
        val playerOrdinal = modifiedPlayers[playerIndex].ordinal
        if (playerOrdinal == null) return
        modifiedPlayers[playerIndex] =
            modifiedPlayers[playerIndex].copy(isPlaying = false, ordinal = null)
        modifiedPlayers = modifiedPlayers.map { player ->
            if (player.isPlaying && player.ordinal != null && player.ordinal > playerOrdinal) {
                player.copy(
                    ordinal = player.ordinal - 1
                )
            } else {
                player
            }
        }.toMutableList()

        _state.value = _state.value.copy(
            availablePlayers = modifiedPlayers
        )
    }

    fun togglePlayer(playerIndex: Int) {
        if (_state.value.availablePlayers[playerIndex].isPlaying) {
            removePlayer(playerIndex)
        } else {
            addPlayer(playerIndex)
        }
    }

    fun getNextPlayerNumber(): Int {
        return _state.value.availablePlayers.count { it.isPlaying } + 1
    }

    fun confirmPlayers() {
        val selectedPlayers = _state.value.availablePlayers.filter { player -> player.isPlaying }
            .sortedBy { player -> player.ordinal }
        if (selectedPlayers.size > 1) {
            _state.value = _state.value.copy(
                selectedPlayers = selectedPlayers,
                gamePhase = GamePhase.DLCSelection
            )
        }
    }

    fun countSelectedPlayers(): Int {
        return _state.value.availablePlayers.count { it.isPlaying }
    }

    fun confirmDLCSelection() {
        _state.value = _state.value.copy(
            gamePhase = GamePhase.PointInput
        )
        initializePoints()
    }

    fun initializePoints() {
        val points = BasePointTypes.entries.flatMap { pointType ->
            _state.value.selectedPlayers.map { player ->
                PlayerPointTypeModel(
                    playerID = player.id,
                    playerName = player.name,
                    pointType = pointType,
                    value = ""
                )
            }
        }.toMutableList()
        if(_state.value.leadersDLC){
            val leadersPoints = listOf(
                PlayerPointTypeModel(
                    playerID = -1,
                    playerName = "Leader Cards",
                    pointType = LeaderPointTypes.LeaderCards,
                    value = ""
                )
            ).flatMap { pointType ->
                _state.value.selectedPlayers.map { player ->
                    pointType.copy(
                        playerID = player.id,
                        playerName = player.name
                    )
                }
            }
            points.addAll(leadersPoints)
        }
        if(_state.value.citiesDLC){
            val citiesPoints = CityPointTypes.entries.flatMap { pointType ->
                _state.value.selectedPlayers.map { player ->
                    PlayerPointTypeModel(
                        playerID = player.id,
                        playerName = player.name,
                        pointType = pointType,
                        value = ""
                    )
                }
            }
            points.addAll(citiesPoints)
        }
        if(_state.value.armadaDLC){
            val armadaPoints = ArmadaPointTypes.entries.flatMap { pointType ->
                _state.value.selectedPlayers.map { player ->
                    PlayerPointTypeModel(
                        playerID = player.id,
                        playerName = player.name,
                        pointType = pointType,
                        value = ""
                    )
                }
            }
            points.addAll(armadaPoints)
        }
        val currentInputPoint = points.popOrNull()
        _state.value = _state.value.copy(pointQueue = points, currentInputPoint = currentInputPoint)
    }

    fun updateCurrentPointValue(value: String) {
        if (value.isEmpty()) {
            _state.value = _state.value.copy(
                currentInputPoint = _state.value.currentInputPoint?.copy(
                    value = ""
                )
            )
        } else {
            try {
                _state.value = _state.value.copy(
                    currentInputPoint = _state.value.currentInputPoint?.copy(
                        value = value
                    )
                )
            } catch (e: Exception) {
                println(e.message)
                _state.value = _state.value.copy(
                    currentInputPoint = _state.value.currentInputPoint?.copy(
                        value = ""
                    )
                )
            }
        }
    }

    fun getCurrentPointValueString(): String {
        val currentPoint = _state.value.currentInputPoint
        return currentPoint?.value ?: ""
    }

    fun insertPointValue() {
        val pointQueueMutable = _state.value.pointQueue.toMutableList()
        val inputValue = _state.value.currentInputPoint
        if (inputValue == null) return
        val confirmedPointsMutable = _state.value.confirmedPoints.toMutableList()
        confirmedPointsMutable.push(inputValue)
        val newCurrentPoint = pointQueueMutable.popOrNull()
        _state.value = _state.value.copy(
            pointQueue = pointQueueMutable,
            confirmedPoints = confirmedPointsMutable,
            currentInputPoint = newCurrentPoint
        )
        if (_state.value.currentInputPoint == null) {
            goToConfirmation()
        }
    }

    fun rollBackPointValue() {
        if (_state.value.confirmedPoints.isEmpty()) return
        val confirmedPointsMutable = _state.value.confirmedPoints.toMutableList()
        val currentInputPoint = _state.value.currentInputPoint
        if (currentInputPoint == null) return
        val newCurrentInputPoint = confirmedPointsMutable.popOrNull()
        val pointQueueMutable = _state.value.pointQueue.toMutableList()
        pointQueueMutable.push(currentInputPoint)
        _state.value = _state.value.copy(
            pointQueue = pointQueueMutable,
            confirmedPoints = confirmedPointsMutable,
            currentInputPoint = newCurrentInputPoint
        )
    }

    fun finishGame() {
        viewModelScope.launch {
            calculateResults()
            _state.value = _state.value.copy(
                gamePhase = GamePhase.Results
            )
            saveGame()
        }
    }

    fun calculateResults() {
        val scoreList = getFinalScoresList()

        _state.value = _state.value.copy(results = scoreList)
    }

    private fun goToConfirmation() {
        calculateResults()
        _state.value = _state.value.copy(gamePhase = GamePhase.Confirmation)
    }

    fun submitGame() {
        viewModelScope.launch {
            _state.value = _state.value.copy(gamePhase = GamePhase.Results)
            saveGame()
        }
    }

    fun backToPointInput() {
        val lastConfirmed = _state.value.confirmedPoints.firstOrNull() ?: return
        _state.value = _state.value.copy(
            confirmedPoints = _state.value.confirmedPoints.drop(1),
            currentInputPoint = lastConfirmed,
            gamePhase = GamePhase.PointInput,
            results = emptyList()
        )
    }

    fun saveGame() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val gameID = gameRepository.addGame(
                    GameModel(
                        id = null,
                        date = Calendar.getInstance().timeInMillis,
                        playerScores = emptyList(),
                        isPrivate = _state.value.isPrivate
                    )
                )
                val finalScores = _state.value.results
                if (finalScores.isEmpty()) {
                    throw Exception("Error: final scores should be calculated at this point")
                }
                finalScores.forEach {
                    playerResultRepository.addPlayerResult(it, gameID)
                }
                _state.value = _state.value.copy(isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = mapToUserMessage(e))
            }
        }
    }

    fun getFinalScoresList(): List<PlayerResultModel> {
        return _state.value.selectedPlayers
            .map { player ->
                val playerScores = _state.value.confirmedPoints
                    .filter { score -> score.playerID == player.id }
                    .map { score -> Pair(score.pointType, score.value.toIntOrNull() ?: 0) }
                val playerTotalScore = playerScores.sumOf { it.second }
                PlayerResultModel(player.id, player.name, playerTotalScore, 0, playerScores)
            }.sortedByDescending {
                it.totalScore
            }.mapIndexed { index, result ->
                result.copy(placement = index + 1)
            }
    }

    fun toggleCitiesDLC() {
        val newValue = !_state.value.citiesDLC
        _state.value = _state.value.copy(citiesDLC = newValue)
    }

    fun toggleArmadaDLC() {
        val newValue = !_state.value.armadaDLC
        _state.value = _state.value.copy(armadaDLC = newValue)
    }

    fun toggleLeadersDLC() {
        val newValue = !_state.value.leadersDLC
        _state.value = _state.value.copy(leadersDLC = newValue)
    }

    fun toggleIsPrivate() {
        _state.value = _state.value.copy(isPrivate = !_state.value.isPrivate)
    }

    fun toggleGreenCardsCalculatorPopup() {
        val newValue = !_state.value.showGreepCardsCalculatorPopup
        _state.value = _state.value.copy(showGreepCardsCalculatorPopup = newValue)
    }
}


fun <T> MutableList<T>.push(item: T) {
    add(0, item)
}

fun <T> MutableList<T>.popOrNull(): T? {
    return if (isEmpty()) null else removeAt(0)
}