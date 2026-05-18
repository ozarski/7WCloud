package com.example.the7wonders.ui.tabsScreen.playersTab

import androidx.compose.foundation.lazy.grid.LazyGridState
import com.example.the7wonders.domain.model.PlayerModel

data class PlayerListState(
    val playerList: List<PlayerModel>,
    val isLoading: Boolean = false,
    val gridState: LazyGridState = LazyGridState(),
    val deletePopupVisible: Boolean = false,
    val popupPlayerModel: PlayerModel? = null,
    val error: String? = null
)
