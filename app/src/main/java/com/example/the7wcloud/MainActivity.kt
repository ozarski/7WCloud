package com.example.the7wcloud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.the7wcloud.domain.model.AuthState
import com.example.the7wcloud.ui.Screens
import com.example.the7wcloud.ui.addGameScreen.AddGameScreen
import com.example.the7wcloud.ui.adminScreen.AdminPanelScreen
import com.example.the7wcloud.ui.authScreen.AuthViewModel
import com.example.the7wcloud.ui.authScreen.LoginScreen
import com.example.the7wcloud.ui.base.LoadingScreen
import com.example.the7wcloud.ui.gameDetailsScreen.GameDetailsScreen
import com.example.the7wcloud.ui.tabsScreen.MainTabsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity :
    ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            enableEdgeToEdge()
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState = authViewModel.state.value.authState

    when (authState) {
        is AuthState.Loading -> {
            LoadingScreen()
        }
        is AuthState.Unauthenticated -> {
            LoginScreen(viewModel = authViewModel)
        }
        is AuthState.Authenticated -> {
            val navController = rememberNavController()
            Column {
                NavHost(
                    navController = navController,
                    startDestination = Screens.MainTabs.route,
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None }
                ) {
                    composable(Screens.MainTabs.route) {
                        MainTabsScreen(navController = navController)
                    }
                    composable(
                        Screens.AddGame.route,
                        enterTransition = {
                            slideInVertically(
                                initialOffsetY = { it },
                            )
                        },
                        exitTransition = {
                            slideOutVertically(
                                targetOffsetY = { it },
                            )
                        },
                    ) {
                        AddGameScreen(navController = navController)
                    }
                    composable(
                        Screens.GameDetails.route + "/{${Screens.GAME_DETAILS_ID_PARAM}}",
                        enterTransition = {
                            slideInVertically(
                                initialOffsetY = { it },
                            )
                        },
                        exitTransition = {
                            slideOutVertically(
                                targetOffsetY = { it },
                            )
                        },
                    ) {
                        GameDetailsScreen(navController)
                    }
                    composable(
                        Screens.AdminPanel.route,
                        enterTransition = {
                            slideInVertically(
                                initialOffsetY = { it },
                            )
                        },
                        exitTransition = {
                            slideOutVertically(
                                targetOffsetY = { it },
                            )
                        },
                    ) {
                        AdminPanelScreen(navController = navController)
                    }
                }
            }
        }
    }
}