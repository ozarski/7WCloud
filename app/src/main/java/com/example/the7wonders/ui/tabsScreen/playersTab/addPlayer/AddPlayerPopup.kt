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
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.the7wonders.ui.base.BaseInputField
import com.example.the7wonders.ui.base.BasePopupContainer
import com.example.the7wonders.ui.base.PrimaryButton
import com.example.the7wonders.ui.theme.BaseColors
import com.example.the7wonders.ui.theme.Dimens
import com.example.the7wonders.ui.theme.Transparency

@Composable
fun AddPlayerPopup(
    onDismiss: () -> Unit,
    onAdd: (String, Boolean) -> Unit
) {
    val playerName = remember { mutableStateOf("") }
    val isPrivate = remember { mutableStateOf(false) }

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
            Spacer(modifier = Modifier.size(Dimens.paddingMedium))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isPrivate.value = !isPrivate.value },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if (isPrivate.value) Icons.Outlined.Lock else Icons.Outlined.LockOpen,
                        "private toggle",
                        tint = if (isPrivate.value) BaseColors.secondary
                        else BaseColors.secondaryDark.copy(alpha = Transparency.TRANSPARENCY_30)
                    )
                    Spacer(modifier = Modifier.size(Dimens.paddingMedium))
                    Text(
                        "Private",
                        color = if (isPrivate.value) BaseColors.textPrimary
                        else BaseColors.textSecondary.copy(alpha = Transparency.TRANSPARENCY_50)
                    )
                }
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
                PrimaryButton(
                    label = "Add",
                    modifier = Modifier.weight(1f),
                    buttonColor = BaseColors.onSecondary
                ) {
                    onAdd(playerName.value, isPrivate.value)
                }
            }
            Spacer(modifier = Modifier.size(Dimens.paddingMedium))
        }
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