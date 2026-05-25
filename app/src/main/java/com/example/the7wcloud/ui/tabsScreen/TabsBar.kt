package com.example.the7wcloud.ui.tabsScreen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.the7wcloud.ui.base.BaseFloatingActionButton
import com.example.the7wcloud.ui.theme.BaseColors
import com.example.the7wcloud.ui.theme.Dimens
import com.example.the7wcloud.ui.theme.Transparency
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TabsBar(
    modifier: Modifier = Modifier,
    viewModel: MainTabsViewModel = hiltViewModel(),
    onGameAdd: () -> Unit,
    onPlayerAdd: () -> Unit,
    onTabSelected: (tab: MainTabs) -> Unit
) {
    val state = viewModel.state.value
    Box(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = Dimens.paddingExtraLarge, vertical = Dimens.paddingMedium)
                .shadow(Dimens.elevationMedium, shape = RoundedCornerShape(Dimens.cornerRadiusMax))
                .background(
                    shape = RoundedCornerShape(
                        Dimens.cornerRadiusMax
                    ), color = BaseColors.secondaryDark.copy(alpha = Transparency.TRANSPARENCY_90)
                )
                .align(alignment = Alignment.BottomCenter)
        ) {
            TabItem(state.selectedTab, MainTabs.Games, onTabSelected)
            Spacer(modifier = Modifier.width(Dimens.tabBarSpacing))
            TabItem(state.selectedTab, MainTabs.Players, onTabSelected)
        }
        AddButton(
            modifier = Modifier.align(alignment = Alignment.Center),
            onPlayerAdd,
            onGameAdd,
            onHold = {
                viewModel.showSettingsPopup()
            }
        )
    }
}


@Composable
fun AddButton(
    modifier: Modifier,
    onPlayerAdd: () -> Unit,
    onGameAdd: () -> Unit,
    onHold: () -> Unit,
    viewModel: MainTabsViewModel = hiltViewModel()
) {
    val interactionSource = remember { MutableInteractionSource() }

    val viewConfiguration = LocalViewConfiguration.current


    LaunchedEffect(interactionSource) {
        var isLongClick = false

        interactionSource.interactions.collectLatest { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    isLongClick = false
                    delay(viewConfiguration.longPressTimeoutMillis)
                    isLongClick = true
                    onHold()
                }

                is PressInteraction.Release -> {
                    if (isLongClick.not()) {
                        if (viewModel.state.value.selectedTab == MainTabs.Games) {
                            onGameAdd()
                        } else {
                            onPlayerAdd()
                        }
                    }

                }

                is PressInteraction.Cancel -> {
                    isLongClick = false
                }
            }
        }
    }
    BaseFloatingActionButton(
        onClick = { },
        modifier = modifier
            .padding(bottom = Dimens.paddingExtraLarge),
        color = BaseColors.secondary,
        iconColor = BaseColors.secondaryDark,
        icon = Icons.Filled.Add,
        interactionSource = interactionSource
    )
}

@Composable
fun TabItem(selected: MainTabs, screen: MainTabs, onTabSelected: (MainTabs) -> Unit) {
    val isSelected = screen == selected
    val animatedColor = animateColorAsState(
        targetValue = if (isSelected) BaseColors.secondary.copy(alpha = Transparency.TRANSPARENCY_90) else Color.Transparent,
        animationSpec = tween(Dimens.ANIMATION_DURATION_MEDIUM)
    )
    Box(
        modifier = Modifier
            .padding(
                vertical = Dimens.paddingMedium,
                horizontal = Dimens.paddingMedium
            )
            .clickable(
                interactionSource = null,
                onClick = {
                    onTabSelected(screen)
                },
                indication = null
            )
            .background(
                shape = RoundedCornerShape(Dimens.cornerRadiusMax),
                color = animatedColor.value
            ),
    ) {
        Text(
            screen.name,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            modifier = Modifier
                .widthIn(Dimens.tabItemMinWidth)
                .padding(
                    vertical = Dimens.paddingMedium,
                ),
            color = if (isSelected) BaseColors.secondaryDark else BaseColors.primary,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Light,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview
fun TabsBarPreview() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxSize()
    ) {
        TabsBar(onGameAdd = {}, onPlayerAdd = {}) { tab ->
            println("selected tab ${tab.name} ")
        }
    }
}