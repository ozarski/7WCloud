package com.example.the7wonders.ui.tabsScreen.playersTab.addPlayer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.the7wonders.ui.base.BaseInputField
import com.example.the7wonders.ui.base.BasePopupContainer
import com.example.the7wonders.ui.base.PrimaryButton
import com.example.the7wonders.ui.theme.BaseColors
import com.example.the7wonders.ui.theme.Dimens
import com.example.the7wonders.ui.theme.Transparency
import com.example.the7wonders.ui.theme.Typography

@Composable
fun AddPlayerPopup(
    onDismiss: () -> Unit,
    onAdd: (String, Boolean) -> Unit
) {
    val playerName = remember { mutableStateOf("") }
    val isPrivate = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(false) }

    BasePopupContainer(
        onDismiss = onDismiss
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .width(intrinsicSize = IntrinsicSize.Min)
                .padding(horizontal = Dimens.paddingSmall)
        ) {
            Icon(
                Icons.Outlined.Person,
                "add player popup icon",
                modifier = Modifier.size(Dimens.addPlayerPopupIconSize),
                tint = BaseColors.secondary
            )
            Spacer(modifier = Modifier.size(Dimens.paddingLarge))
            BaseInputField(
                value = playerName.value,
                hint = "player name",
            ) { newName ->
                playerName.value = newName.text
            }
            Spacer(modifier = Modifier.size(Dimens.paddingLarge))
            PrivacyToggle(
                isPrivate = isPrivate,
            ) {
                isPrivate.value = it
            }
            Spacer(modifier = Modifier.size(Dimens.paddingLarge))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PrimaryButton(
                    label = "Cancel",
                    modifier = Modifier.weight(1f)
                ) {
                    onDismiss()
                }
                Spacer(modifier = Modifier.size(Dimens.paddingMedium))
                if (isLoading.value) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(horizontal = Dimens.paddingMedium),
                        color = BaseColors.primary
                    )
                } else {
                    PrimaryButton(
                        label = "Add",
                        modifier = Modifier.weight(1f),
                        buttonColor = BaseColors.onSecondary
                    ) {
                        isLoading.value = true
                        onAdd(playerName.value, isPrivate.value)
                    }
                }
            }
            Spacer(modifier = Modifier.size(Dimens.paddingMedium))
        }
    }
}

@Composable
fun PrivacyToggle(
    isPrivate: MutableState<Boolean>,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(indication = null, interactionSource = null) { onToggle(!isPrivate.value) }
            .padding(horizontal = Dimens.paddingSmall),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Private",
            color = BaseColors.textSecondary,
            style = Typography.labelLarge
        )
        Spacer(modifier = Modifier.size(Dimens.paddingMedium))
        Switch(
            checked = isPrivate.value,
            onCheckedChange = { onToggle(it) },
            colors = androidx.compose.material3.SwitchDefaults.colors(
                checkedThumbColor = BaseColors.primary,
                uncheckedThumbColor = BaseColors.textSecondary.copy(alpha = Transparency.TRANSPARENCY_50),
                checkedTrackColor = BaseColors.onSecondary.copy(alpha = Transparency.TRANSPARENCY_70),
                uncheckedTrackColor = Color.Transparent
            )
        )
    }
}

@Composable
@Preview
fun AddPlayerPopupPreview() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AddPlayerPopup(
            onDismiss = { println("add player popup dismissed") },
            onAdd = { name, _ -> println("player $name added") }
        )
    }
}