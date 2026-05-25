package com.example.the7wcloud.ui.gameDetailsScreen

import com.example.the7wcloud.domain.model.GameDetailsModel

data class GameDetailsState(
    val isLoading: Boolean = false,
    val gameDetails: GameDetailsModel,
    val error: String? = null,
    val toggleError: String? = null,
    val canTogglePrivacy: Boolean = false
)