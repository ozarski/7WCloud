package com.example.the7wonders.ui.tabsScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.the7wonders.R
import com.example.the7wonders.ui.base.BasePopupContainer
import com.example.the7wonders.ui.base.PrimaryButton
import com.example.the7wonders.ui.theme.BaseColors
import com.example.the7wonders.ui.theme.Dimens
import com.example.the7wonders.ui.theme.Transparency
import com.example.the7wonders.ui.theme.Typography

@Composable
fun SettingsPopup(
    viewModel: MainTabsViewModel = hiltViewModel(),
    onSignOut: () -> Unit = {}
) {
    val signOutInteractionSource = remember { MutableInteractionSource() }
    BasePopupContainer(
        onDismiss = {
            viewModel.hideSettingsPopup()
        }
    ) {
        Column(modifier = Modifier.widthIn(max = 250.dp)) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(
                    horizontal = Dimens.paddingMedium,
                    vertical = Dimens.paddingMedium
                )
            ) {
                Icon(
                    painterResource(R.drawable.outline_settings_24),
                    "settings icon",
                    modifier = Modifier.size(Typography.titleLarge.lineHeight.value.dp),
                    tint = BaseColors.textSecondary
                )
                Spacer(modifier = Modifier.size(Dimens.paddingExtraLarge))
                Text("Settings", style = Typography.titleLarge, color = BaseColors.textSecondary)
            }
            Spacer(modifier = Modifier.size(Dimens.paddingMedium))
            SignOutButton(onSignOut = onSignOut)
        }
    }
}

@Composable
fun SignOutButton(
    onSignOut: () -> Unit
) {
    PrimaryButton(
        label = "Sign Out",
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.paddingMedium),
        buttonColor = BaseColors.error,
        textColor = BaseColors.textPrimary
    ) {
        onSignOut()
    }
}
