package com.example.the7wonders.ui.addGameScreen.pickDLCs

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.the7wonders.R
import com.example.the7wonders.ui.addGameScreen.AddGameViewModel
import com.example.the7wonders.ui.base.BaseCheckbox
import com.example.the7wonders.ui.base.PrimaryButton
import com.example.the7wonders.ui.theme.BaseColors
import com.example.the7wonders.ui.theme.Dimens
import com.example.the7wonders.ui.theme.Transparency

@Composable
fun PickDLCsScreen(viewModel: AddGameViewModel = hiltViewModel()) {
    val state = viewModel.state.value

    val borderBrush = Brush.verticalGradient(
        listOf(
            BaseColors.onSecondary.copy(alpha = Transparency.TRANSPARENCY_30),
            BaseColors.secondary.copy(alpha = Transparency.TRANSPARENCY_10),
        )
    )


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 350.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painterResource(R.drawable.puzzle),
                null,
                modifier = Modifier.size(Dimens.addPlayersIconSize),
                tint = BaseColors.secondaryDark
            )
            Spacer(modifier = Modifier.size(Dimens.paddingExtraLarge))

            Card(
                modifier = Modifier
                    .heightIn(max = Dimens.addPlayerListMaxHeight)
                    .width(Dimens.addPlayerListWidth),
                border = BorderStroke(Dimens.strokeWidthMedium, borderBrush),
                shape = RoundedCornerShape(Dimens.cornerRadiusExtraLarge),
                colors = CardDefaults.cardColors(
                    containerColor = BaseColors.primary.copy(alpha = Transparency.TRANSPARENCY_10)
                )
            ) {
                Spacer(
                    modifier = Modifier.size(Dimens.paddingLarge)
                )
                DLCToggleButton(
                    dlcName = "Leaders",
                    isSelected = state.leadersDLC,
                    checkedIcon = painterResource(R.drawable.queen),
                    onToggle = { viewModel.toggleLeadersDLC() }
                )
                DLCToggleButton(
                    dlcName = "Cities",
                    isSelected = state.citiesDLC,
                    checkedIcon = painterResource(R.drawable.city),
                    onToggle = { viewModel.toggleCitiesDLC() }
                )
                DLCToggleButton(
                    dlcName = "Armada",
                    isSelected = state.armadaDLC,
                    checkedIcon = painterResource(R.drawable.boat),
                    onToggle = { viewModel.toggleArmadaDLC() }
                )
                DLCToggleButton(
                    dlcName = "Private Game",
                    isSelected = state.isPrivate,
                    checkedIcon = rememberVectorPainter(Icons.Outlined.Lock),
                    onToggle = { viewModel.toggleIsPrivate() }
                )
                Spacer(
                    modifier = Modifier.size(Dimens.paddingLarge)
                )
            }

            Spacer(
                modifier = Modifier.size(Dimens.paddingLarge)
            )

            PrimaryButton(
                label = stringResource(R.string.continue_button_label),
                buttonColor = BaseColors.secondaryDark,
                textColor = BaseColors.textSecondary,
                modifier = Modifier.width(Dimens.addPlayerListWidth)
            ) {
                viewModel.confirmDLCSelection()
            }
            Spacer(modifier = Modifier.size(Dimens.dlcScreenBottomPadding))
        }
    }
}

@Composable
fun DLCToggleButton(
    dlcName: String,
    isSelected: Boolean,
    checkedIcon: Painter,
    onToggle: () -> Unit
) {
    Crossfade(
        targetState = isSelected
    ) { isSelected ->
        BaseCheckbox(
            modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens.paddingLarge),
            checked = isSelected,
            checkedIcon = checkedIcon,
            uncheckedIcon = painterResource(R.drawable.close_circle),
            label = dlcName,
            colorUnchecked = BaseColors.secondaryDark.copy(alpha = Transparency.TRANSPARENCY_30),
            colorChecked = BaseColors.secondary.copy(alpha = Transparency.TRANSPARENCY_70),
            contentColorUnchecked = BaseColors.textSecondary.copy(alpha = Transparency.TRANSPARENCY_50),
            contentColorChecked = BaseColors.textPrimary
        ) {
            onToggle()
        }
    }
}