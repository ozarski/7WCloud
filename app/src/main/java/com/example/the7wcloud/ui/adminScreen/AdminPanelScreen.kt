package com.example.the7wcloud.ui.adminScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.activity.compose.BackHandler
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.the7wcloud.domain.model.AdminUserModel
import com.example.the7wcloud.ui.base.AlertPopup
import com.example.the7wcloud.ui.base.BackgroundOrientation
import com.example.the7wcloud.ui.base.BaseBackground
import com.example.the7wcloud.ui.base.ConfirmationPopup
import com.example.the7wcloud.ui.base.ErrorWidget
import com.example.the7wcloud.ui.base.LoadingScreen
import com.example.the7wcloud.ui.base.PrimaryButton
import com.example.the7wcloud.ui.theme.BaseColors
import com.example.the7wcloud.ui.theme.Dimens
import com.example.the7wcloud.ui.theme.Transparency
import com.example.the7wcloud.ui.theme.Typography

@Composable
fun AdminPanelScreen(
    navController: NavController,
    viewModel: AdminPanelViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    BackHandler {
        if (state.gamesDeleted) {
            navController.previousBackStackEntry?.savedStateHandle?.set("gamesDeleted", true)
        }
        if (state.playersDeleted) {
            navController.previousBackStackEntry?.savedStateHandle?.set("playersDeleted", true)
        }
        navController.popBackStack()
    }

    if (state.actionError != null) {
        AlertPopup(
            title = "Error",
            message = state.actionError,
            onDismiss = { viewModel.clearActionError() }
        )
    }

    if (state.showDeleteGamesConfirmation != null) {
        val user = state.users.find { it.id == state.showDeleteGamesConfirmation }
        ConfirmationPopup(
            title = "Delete Games",
            message = "Are you sure you want to delete all games by ${user?.displayName ?: "this user"}? This action cannot be undone.",
            positiveButtonText = "Delete",
            negativeButtonText = "Cancel",
            onPositiveClick = { user?.let { viewModel.deleteUserGames(it.id) } },
            onNegativeClick = { viewModel.dismissDeleteGamesConfirmation() }
        )
    }

    if (state.showDeletePlayersConfirmation != null) {
        val user = state.users.find { it.id == state.showDeletePlayersConfirmation }
        ConfirmationPopup(
            title = "Delete Players",
            message = "Are you sure you want to delete all players created by ${user?.displayName ?: "this user"}? This will hide them from the player list but won't affect existing games.",
            positiveButtonText = "Delete",
            negativeButtonText = "Cancel",
            onPositiveClick = { user?.let { viewModel.deleteUserPlayers(it.id) } },
            onNegativeClick = { viewModel.dismissDeletePlayersConfirmation() }
        )
    }

    if (state.showDeleteAccountConfirmation != null) {
        val user = state.users.find { it.id == state.showDeleteAccountConfirmation }
        ConfirmationPopup(
            title = "Delete Account",
            message = "Are you sure you want to permanently delete ${user?.displayName ?: "this user"}'s account and all of their data? This action cannot be undone.",
            positiveButtonText = "Delete",
            negativeButtonText = "Cancel",
            onPositiveClick = { user?.let { viewModel.deleteUserAccount(it.id) } },
            onNegativeClick = { viewModel.dismissDeleteAccountConfirmation() }
        )
    }

    BaseBackground(orientation = BackgroundOrientation.Horizontal) {
        when {
            state.isLoading -> LoadingScreen(modifier = Modifier.fillMaxSize())
            state.error != null -> ErrorWidget(
                message = state.error,
                onRetry = { viewModel.loadUsers() }
            )

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = Dimens.paddingMedium,
                            vertical = Dimens.paddingLarge
                        )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier
                                .clickable {
                                    if (state.gamesDeleted) {
                                        navController.previousBackStackEntry?.savedStateHandle?.set("gamesDeleted", true)
                                    }
                                    if (state.playersDeleted) {
                                        navController.previousBackStackEntry?.savedStateHandle?.set("playersDeleted", true)
                                    }
                                    navController.popBackStack()
                                }
                                .padding(Dimens.paddingMedium)
                                .size(Dimens.iconSizeMedium),
                            tint = BaseColors.secondary
                        )
                        Spacer(modifier = Modifier.size(Dimens.paddingMedium))
                        Text(
                            "Admin Panel",
                            style = Typography.titleLarge,
                            color = BaseColors.secondary
                        )
                    }

                    Spacer(modifier = Modifier.size(Dimens.paddingLarge))

                    Text(
                        "${state.users.size} user${if (state.users.size != 1) "s" else ""}",
                        style = Typography.labelMedium,
                        color = BaseColors.textSecondary,
                        modifier = Modifier.padding(horizontal = Dimens.paddingMedium)
                    )

                    Spacer(modifier = Modifier.size(Dimens.paddingMedium))

                    LazyColumn {
                        items(state.users) { user ->
                            AdminUserItem(
                                user = user,
                                isCurrentUser = user.id == state.currentUserId,
                                isActionLoading = user.id == state.actionLoadingUserId,
                                isDeleteGamesLoading = user.id == state.deleteGamesLoadingUserId,
                                isDeletePlayersLoading = user.id == state.deletePlayersLoadingUserId,
                                isDeleteAccountLoading = user.id == state.deleteAccountLoadingUserId,
                                onToggleRole = {
                                    val newRole = if (user.role == "admin") "user" else "admin"
                                    viewModel.setUserRole(user.id, newRole)
                                },
                                onDeleteGames = { viewModel.confirmDeleteGames(user.id) },
                                onDeletePlayers = { viewModel.confirmDeletePlayers(user.id) },
                                onDeleteAccount = { viewModel.confirmDeleteAccount(user.id) }
                            )
                            Spacer(modifier = Modifier.size(Dimens.paddingMedium))
                        }
                        item {
                            Spacer(modifier = Modifier.size(Dimens.lazyColumnBottomSpacing))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminUserItem(
    user: AdminUserModel,
    isCurrentUser: Boolean,
    isActionLoading: Boolean,
    isDeleteGamesLoading: Boolean = false,
    isDeletePlayersLoading: Boolean = false,
    isDeleteAccountLoading: Boolean = false,
    onToggleRole: () -> Unit,
    onDeleteGames: () -> Unit = {},
    onDeletePlayers: () -> Unit = {},
    onDeleteAccount: () -> Unit = {}
) {
    val borderBrush = Brush.horizontalGradient(
        listOf(
            BaseColors.secondary.copy(alpha = Transparency.TRANSPARENCY_10),
            BaseColors.onSecondary.copy(alpha = Transparency.TRANSPARENCY_30),
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.paddingSmall),
        colors = CardDefaults.cardColors(
            containerColor = BaseColors.secondaryDark.copy(alpha = Transparency.TRANSPARENCY_30)
        ),
        border = BorderStroke(width = Dimens.strokeWidthMedium, brush = borderBrush),
        shape = RoundedCornerShape(Dimens.cornerRadiusExtraLarge)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = Dimens.paddingLarge,
                    vertical = Dimens.paddingMedium
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Dimens.iconSizeLarge)
                    .clip(CircleShape)
                    .background(BaseColors.onSecondary.copy(alpha = Transparency.TRANSPARENCY_30)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(Dimens.iconSizeMedium),
                    tint = BaseColors.secondary
                )
            }

            Spacer(modifier = Modifier.size(Dimens.paddingMedium))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = user.displayName ?: "Unknown",
                        style = Typography.labelLarge,
                        color = BaseColors.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.widthIn(max = 160.dp)
                    )
                    if (isCurrentUser) {
                        Spacer(modifier = Modifier.size(Dimens.paddingSmall))
                        Text(
                            text = "(you)",
                            style = Typography.bodyMedium,
                            color = BaseColors.textSecondary
                        )
                    }
                }

                if (user.email != null) {
                    Text(
                        text = user.email,
                        style = Typography.bodyMedium,
                        color = BaseColors.textSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.size(Dimens.paddingSmall))

                Text(
                    text = if (user.role == "admin") "Admin" else "User",
                    style = Typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (user.role == "admin") BaseColors.winIconColor else BaseColors.textSecondary
                )
            }
        }

        if (!isCurrentUser) {
            Spacer(modifier = Modifier.size(Dimens.paddingSmall))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.paddingLarge),
                horizontalArrangement = Arrangement.spacedBy(Dimens.paddingSmall)
            ) {
                if (isActionLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .weight(1f)
                            .size(Dimens.iconSizeMedium),
                        color = BaseColors.secondary,
                        strokeWidth = 3.dp
                    )
                } else {
                    PrimaryButton(
                        label = if (user.role == "admin") "Remove Admin" else "Make Admin",
                        buttonColor = if (user.role == "admin") BaseColors.error else BaseColors.success,
                        textColor = BaseColors.primary,
                        modifier = Modifier.weight(1f),
                        onClick = onToggleRole
                    )
                }

                if (isDeleteAccountLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .weight(1f)
                            .size(Dimens.iconSizeMedium),
                        color = BaseColors.secondary,
                        strokeWidth = 3.dp
                    )
                } else {
                    PrimaryButton(
                        label = "Delete Account",
                        buttonColor = BaseColors.error,
                        textColor = BaseColors.primary,
                        modifier = Modifier.weight(1f),
                        onClick = onDeleteAccount
                    )
                }
            }

            Spacer(modifier = Modifier.size(Dimens.paddingSmall))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = Dimens.paddingLarge,
                        end = Dimens.paddingLarge,
                        bottom = Dimens.paddingMedium
                    ),
                horizontalArrangement = Arrangement.spacedBy(Dimens.paddingSmall)
            ) {
                if (isDeleteGamesLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .weight(1f)
                            .size(Dimens.iconSizeMedium),
                        color = BaseColors.secondary,
                        strokeWidth = 3.dp
                    )
                } else {
                    PrimaryButton(
                        label = "Delete Games",
                        buttonColor = BaseColors.error,
                        textColor = BaseColors.primary,
                        modifier = Modifier.weight(1f),
                        onClick = onDeleteGames
                    )
                }

                if (isDeletePlayersLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .weight(1f)
                            .size(Dimens.iconSizeMedium),
                        color = BaseColors.secondary,
                        strokeWidth = 3.dp
                    )
                } else {
                    PrimaryButton(
                        label = "Delete Players",
                        buttonColor = BaseColors.error,
                        textColor = BaseColors.primary,
                        modifier = Modifier.weight(1f),
                        onClick = onDeletePlayers
                    )
                }
            }
        }
    }
}
