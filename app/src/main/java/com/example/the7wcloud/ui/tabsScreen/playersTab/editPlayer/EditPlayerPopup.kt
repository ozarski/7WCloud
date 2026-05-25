package com.example.the7wcloud.ui.tabsScreen.playersTab.editPlayer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.the7wcloud.domain.model.PlayerModel
import com.example.the7wcloud.ui.base.BaseInputField
import com.example.the7wcloud.ui.base.BasePopupContainer
import com.example.the7wcloud.ui.base.PrimaryButton
import com.example.the7wcloud.ui.tabsScreen.playersTab.addPlayer.PrivacyToggle
import com.example.the7wcloud.ui.theme.BaseColors
import com.example.the7wcloud.ui.theme.Dimens
import com.example.the7wcloud.ui.theme.Typography

private enum class PopupMode { Editing, DeleteConfirmation }

@Composable
fun EditPlayerPopup(
    player: PlayerModel,
    errorMessage: String?,
    onDismiss: () -> Unit,
    onSave: (String, Boolean) -> Unit,
    onDelete: () -> Unit
) {
    val editedName = remember { mutableStateOf(player.name) }
    val isPrivate = remember { mutableStateOf(player.isPrivate) }
    val mode = remember { mutableStateOf(PopupMode.Editing) }
    val isLoading = remember { mutableStateOf(false) }

    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            isLoading.value = false
            mode.value = PopupMode.Editing
        }
    }

    BasePopupContainer(
        onDismiss = onDismiss
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .widthIn(min = Dimens.editPlayerPopupMinWidth)
                .padding(horizontal = Dimens.paddingSmall)
        ) {
            Icon(
                Icons.Outlined.Person,
                "edit player popup icon",
                modifier = Modifier.size(Dimens.addPlayerPopupIconSize),
                tint = BaseColors.secondary
            )

            if (mode.value == PopupMode.Editing) {
                Spacer(modifier = Modifier.size(Dimens.paddingLarge))
                BaseInputField(
                    value = editedName.value,
                    hint = "player name",
                ) { newName ->
                    editedName.value = newName.text
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.size(Dimens.paddingSmall))
                    Text(
                        text = errorMessage,
                        color = BaseColors.error,
                        style = Typography.labelMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.size(Dimens.paddingMedium))
                PrivacyToggle(isPrivate = isPrivate) {
                    isPrivate.value = it
                }

                Spacer(modifier = Modifier.size(Dimens.paddingMedium))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (isLoading.value) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            CircularProgressIndicator(
                                modifier = Modifier.padding(horizontal = Dimens.paddingMedium),
                                color = BaseColors.primary
                            )
                        }
                    } else {
                        PrimaryButton(
                            label = "Save",
                            modifier = Modifier.weight(1f),
                            buttonColor = BaseColors.onSecondary
                        ) {
                            isLoading.value = true
                            onSave(editedName.value, isPrivate.value)
                        }
                        Spacer(modifier = Modifier.size(Dimens.paddingMedium))
                        PrimaryButton(
                            label = "Cancel",
                            modifier = Modifier.weight(1f)
                        ) {
                            onDismiss()
                        }
                    }
                }

                Spacer(modifier = Modifier.size(Dimens.paddingMedium))
                PrimaryButton(
                    label = "Delete Player",
                    buttonColor = BaseColors.error,
                    textColor = BaseColors.secondary,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    mode.value = PopupMode.DeleteConfirmation
                }
            } else {
                Spacer(modifier = Modifier.size(Dimens.paddingLarge))
                Text(
                    text = "Are you sure?",
                    style = Typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                    color = BaseColors.primary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(Dimens.spacerSizeExtraLarge))
                Text(
                    text = "Player profile will be deleted permanently",
                    style = Typography.labelLarge,
                    color = BaseColors.primary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(Dimens.spacerSizeExtraLarge))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.paddingMedium),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (isLoading.value) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(horizontal = Dimens.paddingMedium),
                            color = BaseColors.primary
                        )
                    } else {
                        PrimaryButton(
                            label = "Yes",
                            buttonColor = BaseColors.error,
                            onClick = {
                                isLoading.value = true
                                onDelete()
                            },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.size(Dimens.paddingMedium))
                        PrimaryButton(
                            label = "No",
                            onClick = { mode.value = PopupMode.Editing },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Spacer(modifier = Modifier.size(Dimens.paddingMedium))
            }
        }
    }
}

@Composable
@Preview
fun EditPlayerPopupPreview() {
    val player = PlayerModel(id = 1, name = "Wojtek", isPrivate = false)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        EditPlayerPopup(
            player = player,
            errorMessage = null,
            onDismiss = { println("dismissed") },
            onSave = { name, isPrivate -> println("save $name $isPrivate") },
            onDelete = { println("delete") }
        )
    }
}
