package com.example.the7wcloud.ui.addGameScreen.playerSelection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.the7wcloud.R
import com.example.the7wcloud.ui.addGameScreen.AddGameViewModel
import com.example.the7wcloud.ui.base.LoadingScreen
import com.example.the7wcloud.ui.base.PrimaryButton
import com.example.the7wcloud.ui.theme.BaseColors
import com.example.the7wcloud.ui.theme.Dimens
import com.example.the7wcloud.ui.theme.Typography

@Composable
fun AddPlayersScreen(viewModel: AddGameViewModel = hiltViewModel()) {
    val state = viewModel.state.value
    if (state.isLoading) {
        LoadingScreen(modifier = Modifier.fillMaxSize())
    } else if (state.availablePlayers.isEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(stringResource(R.string.no_players_found), style = Typography.labelLarge)
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = Dimens.addPlayerScreenPaddingBottom)
        ) {
            Spacer(modifier = Modifier.size(Dimens.paddingExtraLarge))
            Icon(
                Icons.Outlined.Person,
                null,
                modifier = Modifier.size(Dimens.addPlayersIconSize),
                tint = BaseColors.secondaryDark
            )
            Spacer(modifier = Modifier.size(Dimens.paddingExtraLarge))
            AddPlayerList()
            Spacer(modifier = Modifier.size(Dimens.paddingExtraLarge))
            PrimaryButton(
                label = stringResource(R.string.continue_button_label),
                buttonColor = BaseColors.secondaryDark,
                textColor = BaseColors.textSecondary,
                modifier = Modifier.width(Dimens.addPlayerListWidth),
                enabled = viewModel.countSelectedPlayers() > 1
            ) {
                viewModel.confirmPlayers()
            }
        }
    }
}