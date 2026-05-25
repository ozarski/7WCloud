package com.example.the7wcloud.ui.addGameScreen.inputPoints

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.the7wcloud.R
import com.example.the7wcloud.domain.model.BasePointTypes
import com.example.the7wcloud.ui.addGameScreen.AddGameViewModel
import com.example.the7wcloud.ui.base.BaseInputField
import com.example.the7wcloud.ui.base.PrimaryButton
import com.example.the7wcloud.ui.theme.BaseColors
import com.example.the7wcloud.ui.theme.Dimens
import com.example.the7wcloud.ui.theme.Typography

@Composable
fun PointInputScreen(viewModel: AddGameViewModel = hiltViewModel()) {
    val state = viewModel.state.value
    val currentPoint = state.currentInputPoint
    val context = LocalContext.current
    if (currentPoint != null) {
        if (currentPoint.pointType == BasePointTypes.Green) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = Dimens.paddingExtraLarge, end = Dimens.paddingMedium),
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(
                    onClick = {
                        viewModel.toggleGreenCardsCalculatorPopup()
                    },
                    modifier = Modifier.size(Dimens.iconSizeExtraLarge),
                ) {
                    Icon(
                        painterResource(R.drawable.calculator),
                        contentDescription = "Edit green cards points popup",
                        tint = BaseColors.secondary,
                        modifier = Modifier.size(Dimens.iconSizeLarge)
                    )
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(max = 350.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.size(250.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Crossfade(targetState = currentPoint.pointType) { type ->
                        Icon(
                            painterResource(type.icon),
                            "point type icon",
                            tint = type.color,
                            modifier = Modifier.size(Dimens.addPlayersIconSize)
                        )
                    }
                }

                Spacer(modifier = Modifier.size(Dimens.paddingSmall))

                Crossfade(targetState = currentPoint.playerName) { name ->
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.size(Dimens.paddingLarge))
                        Text(
                            stringResource(
                                R.string.point_type_for_player,
                                currentPoint.pointType.pointName,
                                name,
                            ),
                            style = Typography.labelLarge.copy(fontFamily = FontFamily.Monospace),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            color = BaseColors.secondary
                        )
                    }
                }
                Spacer(modifier = Modifier.size(Dimens.paddingLarge))
                BaseInputField(
                    value = viewModel.getCurrentPointValueString(),
                    hint = stringResource(R.string.points_input_hint),
                    onValueChange = {
                        viewModel.updateCurrentPointValue(it.text)
                    },
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.paddingSmall),
                    keyboardAction = {
                        viewModel.insertPointValue()
                    }
                )
                Spacer(modifier = Modifier.size(Dimens.paddingLarge))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                        .padding(horizontal = Dimens.paddingSmall)
                ) {
                    val prevButtonVisible = !state.confirmedPoints.isEmpty()
                    AnimatedVisibility(
                        visible = prevButtonVisible,
                        enter = fadeIn() + expandHorizontally(),
                        exit = fadeOut() + shrinkHorizontally(),
                    ) {
                        PrimaryButton(
                            label = stringResource(R.string.previous_button_label),
                            textColor = BaseColors.secondaryDark,
                            buttonColor = BaseColors.secondary,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = Dimens.paddingMedium)
                        ) {
                            viewModel.rollBackPointValue()
                        }
                    }
                    PrimaryButton(
                        label = stringResource(R.string.next_button_label),
                        buttonColor = BaseColors.secondaryDark,
                        textColor = BaseColors.textSecondary,
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        viewModel.insertPointValue()
                    }
                }
            }
        }
    }

    if (state.showGreepCardsCalculatorPopup) {
        GreenCardsCalculatorPopup(
            onApply = { points ->
                viewModel.updateCurrentPointValue(points.toString())
                viewModel.toggleGreenCardsCalculatorPopup()
            },
            onDismiss = {
                viewModel.toggleGreenCardsCalculatorPopup()
            }
        )
    }
}