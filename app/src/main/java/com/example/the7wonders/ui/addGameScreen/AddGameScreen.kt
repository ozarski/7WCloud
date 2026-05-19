package com.example.the7wonders.ui.addGameScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.the7wonders.ui.addGameScreen.confirmation.ConfirmationScreen
import com.example.the7wonders.ui.addGameScreen.inputPoints.PointInputScreen
import com.example.the7wonders.ui.addGameScreen.pickDLCs.PickDLCsScreen
import com.example.the7wonders.ui.addGameScreen.playerSelection.AddPlayersScreen
import com.example.the7wonders.ui.addGameScreen.results.GameResultsScreen
import com.example.the7wonders.ui.base.BaseBackground
import com.example.the7wonders.ui.base.ErrorWidget

@Composable
fun AddGameScreen(viewModel: AddGameViewModel = hiltViewModel(), navController: NavHostController) {
    val state = viewModel.state.value
    BaseBackground {
        if (state.error != null) {
            ErrorWidget(message = state.error, onRetry = { viewModel.loadAvailablePlayers() })
        } else {
            Crossfade(targetState = state.gamePhase) { gamePhase ->
                when (gamePhase) {
                    GamePhase.PlayerSelection -> {
                        AddPlayersScreen()
                    }

                    GamePhase.DLCSelection -> {
                        PickDLCsScreen()
                    }

                    GamePhase.PointInput -> {
                        PointInputScreen()
                    }

                    GamePhase.Confirmation -> {
                        ConfirmationScreen()
                    }

                    GamePhase.Results -> {
                        GameResultsScreen(navController = navController)
                    }
                }
            }
        }
    }
    BackHandler {
        //TODO("Implement confirmation popup")
        navController.popBackStack()
    }
}