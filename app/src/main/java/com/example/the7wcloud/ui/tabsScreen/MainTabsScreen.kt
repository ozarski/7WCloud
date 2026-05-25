package com.example.the7wcloud.ui.tabsScreen

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.the7wcloud.R
import com.example.the7wcloud.ui.Screens
import com.example.the7wcloud.ui.authScreen.AuthViewModel
import com.example.the7wcloud.ui.base.AlertPopup
import com.example.the7wcloud.ui.base.BackgroundOrientation
import com.example.the7wcloud.ui.base.BaseBackground
import com.example.the7wcloud.ui.tabsScreen.gamesTab.GameListScreen
import com.example.the7wcloud.ui.tabsScreen.playersTab.PlayerListScreen
import com.example.the7wcloud.ui.tabsScreen.playersTab.PlayerListViewModel
import com.example.the7wcloud.ui.tabsScreen.playersTab.addPlayer.AddPlayerPopup
import com.example.the7wcloud.ui.theme.Dimens
import com.example.the7wcloud.ui.util.mapToUserMessage
import kotlinx.coroutines.launch

@Composable
fun MainTabsScreen(
    viewModel: MainTabsViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val state = viewModel.state.value
    val playerListViewModel: PlayerListViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()

    val currentBackStackEntry = navController.currentBackStackEntry
    LaunchedEffect(currentBackStackEntry) {
        currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow("gameAdded", false)
            ?.collect { gameAdded ->
                if (gameAdded) {
                    playerListViewModel.loadPlayers()
                }
            }
    }
    LaunchedEffect(currentBackStackEntry) {
        currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow("gameDeleted", false)
            ?.collect { gameDeleted ->
                if (gameDeleted) {
                    playerListViewModel.loadPlayers()
                    currentBackStackEntry?.savedStateHandle?.set("gameDeleted", false)
                }
            }
    }
    LaunchedEffect(currentBackStackEntry) {
        currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow("playersDeleted", false)
            ?.collect { playersDeleted ->
                if (playersDeleted) {
                    playerListViewModel.loadPlayers()
                    currentBackStackEntry?.savedStateHandle?.set("playersDeleted", false)
                }
            }
    }

    if (state.addPlayerPopupVisible) {
        AddPlayerPopup(
            onDismiss = { viewModel.hideAddPlayerPopup() },
            onAdd = { name, isPrivate ->
                scope.launch {
                    try {
                        viewModel.addPlayer(name, isPrivate)
                        playerListViewModel.loadPlayers()
                        viewModel.hideAddPlayerPopup()
                    } catch (e: Exception) {
                        viewModel.hideAddPlayerPopup()
                        viewModel.setAddPlayerError(mapToUserMessage(e))
                    }
                }
            }
        )
    }
    if (state.addPlayerError != null) {
        AlertPopup(
            title = stringResource(R.string.error),
            message = state.addPlayerError ?: "",
        ) {
            viewModel.clearAddPlayerError()
        }
    }
    if (state.settingsPopupVisible) {
        val authViewModel: AuthViewModel = hiltViewModel()
        SettingsPopup(
            isAdmin = state.isAdmin,
            onSignOut = { authViewModel.signOut() },
            onAdminPanelClick = {
                navController.navigate(Screens.AdminPanel.route)
                viewModel.hideSettingsPopup()
            }
        )
    }

    Box {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BaseBackground(
                modifier = Modifier.fillMaxSize(),
                orientation = BackgroundOrientation.Horizontal
            ) {
                Crossfade(
                    targetState = state.selectedTab,
                    animationSpec = tween(Dimens.ANIMATION_DURATION_SHORT)
                ) { tab ->
                    when (tab) {
                        MainTabs.Games -> {
                            GameListScreen(navController)
                        }

                        MainTabs.Players -> {
                            PlayerListScreen()
                        }
                    }
                }
            }
        }
        TabsBar(
            modifier = Modifier.align(alignment = Alignment.BottomCenter),
            onPlayerAdd = {
                viewModel.showAddPlayerPopup()
            },
            onGameAdd = {
                navController.navigate(Screens.AddGame.route)
            }
        ) { tab ->
            viewModel.selectTab(tab)
        }
    }
}
