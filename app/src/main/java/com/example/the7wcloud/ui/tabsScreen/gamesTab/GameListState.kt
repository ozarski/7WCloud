package com.example.the7wcloud.ui.tabsScreen.gamesTab

import androidx.compose.foundation.lazy.LazyListState
import com.example.the7wcloud.domain.model.GameModel

data class GameListState(
    val gameList: List<GameModel>,
    val isLoading: Boolean = false,
    val lazyListState: LazyListState = LazyListState(),
    val deletePopupVisible: Boolean = false,
    val popupGameModel: GameModel? = null,
    val deletePopupError: String? = null,
    val error: String? = null
)
