package com.example.the7wonders.ui.tabsScreen.playersTab

import androidx.compose.foundation.lazy.grid.LazyGridState
import com.example.the7wonders.domain.model.PlayerModel

data class PlayerListState(
    val playerList: List<PlayerModel>,
    val isLoading: Boolean = false,
    val gridState: LazyGridState = LazyGridState(),
    val editPopupVisible: Boolean = false,
    val editPopupPlayerModel: PlayerModel? = null,
    val editPopupError: String? = null,
    val error: String? = null
)
