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
import com.example.the7wonders.ui.theme.BaseColors
import com.example.the7wonders.ui.theme.Dimens
import com.example.the7wonders.ui.theme.Transparency
import com.example.the7wonders.ui.theme.Typography

@Composable
fun SettingsPopup(
    viewModel: MainTabsViewModel = hiltViewModel(),
    onSignOut: () -> Unit = {}
) {
    val exportInteractionSource = remember { MutableInteractionSource() }
    val importInteractionSource = remember { MutableInteractionSource() }
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
            Spacer(modifier = Modifier.size(Dimens.paddingExtraLarge))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.paddingLarge)
            ) {
                Icon(
                    painterResource(R.drawable.export_icon),
                    "export icon",
                    modifier = Modifier
                        .size(
                            Dimens.iconSizeLarge
                        )
                        .clickable(
                            interactionSource = exportInteractionSource,
                            indication = ripple(
                                color = BaseColors.secondary.copy(alpha = Transparency.TRANSPARENCY_30),
                                bounded = false
                            ),
                            onClick = {
                                viewModel.showExportDatabasePopup()
                            }
                        ),
                    tint = BaseColors.textSecondary
                )
                Spacer(modifier = Modifier.size(Dimens.paddingLarge))
                Icon(
                    painterResource(R.drawable.import_icon),
                    "import icon",
                    modifier = Modifier
                        .size(
                            Dimens.iconSizeLarge
                        )
                        .clickable(
                            interactionSource = importInteractionSource,
                            indication = ripple(
                                color = BaseColors.secondary.copy(alpha = Transparency.TRANSPARENCY_30),
                                bounded = false
                            ),
                            onClick = {
                                viewModel.showImportDatabasePopup()
                            }
                        ),
                    tint = BaseColors.textSecondary
                )
            }
            Spacer(modifier = Modifier.size(Dimens.paddingExtraLarge))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = signOutInteractionSource,
                        indication = ripple(
                            color = BaseColors.secondary.copy(alpha = Transparency.TRANSPARENCY_30),
                            bounded = false
                        ),
                        onClick = {
                            viewModel.hideSettingsPopup()
                            onSignOut()
                        }
                    )
                    .padding(horizontal = Dimens.paddingMedium, vertical = Dimens.paddingMedium),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Sign Out", style = Typography.bodyMedium, color = BaseColors.error)
            }
        }
    }
}