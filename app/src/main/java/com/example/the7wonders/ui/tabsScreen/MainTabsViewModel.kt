package com.example.the7wonders.ui.tabsScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.the7wonders.domain.model.PlayerModel
import com.example.the7wonders.domain.repository.GameRepository
import com.example.the7wonders.domain.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainTabsViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val gameRepository: GameRepository
) :
    ViewModel() {
    private val _state = mutableStateOf(MainTabsState(selectedTab = MainTabs.Games))
    val state: State<MainTabsState> = _state

    init {
        viewModelScope.launch {
            _state.value = _state.value.copy(isAdmin = gameRepository.isAdmin())
        }
    }

    fun selectTab(tab: MainTabs) {
        _state.value = _state.value.copy(selectedTab = tab)
    }

    fun showAddPlayerPopup() {
        _state.value = _state.value.copy(addPlayerPopupVisible = true)
    }

    fun hideAddPlayerPopup() {
        _state.value = _state.value.copy(addPlayerPopupVisible = false)
    }

    fun clearAddPlayerError() {
        _state.value = _state.value.copy(addPlayerError = null)
    }

    fun setAddPlayerError(message: String?) {
        _state.value = _state.value.copy(addPlayerError = message)
    }

    fun showSettingsPopup() {
        _state.value = _state.value.copy(settingsPopupVisible = true)
    }

    fun hideSettingsPopup() {
        _state.value = _state.value.copy(settingsPopupVisible = false)
    }

    suspend fun addPlayer(name: String, isPrivate: Boolean) {
        if (playerRepository.playerExists(name)) {
            throw Exception("Player \"$name\" already exists")
        }
        playerRepository.addPlayer(PlayerModel(name = name, isPrivate = isPrivate))
    }
}
