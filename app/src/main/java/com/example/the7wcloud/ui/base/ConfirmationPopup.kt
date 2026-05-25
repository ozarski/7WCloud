package com.example.the7wcloud.ui.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.the7wcloud.ui.theme.BaseColors
import com.example.the7wcloud.ui.theme.Dimens
import com.example.the7wcloud.ui.theme.Typography

@Composable
fun ConfirmationPopup(
    title: String,
    message: String,
    positiveButtonText: String,
    negativeButtonText: String,
    shouldDisplayLoading: Boolean = false,
    errorMessage: String? = null,
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit,
) {
    val isLoading = remember { mutableStateOf(false) }

    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            isLoading.value = false
        }
    }

    BasePopupContainer(onDismiss = onNegativeClick) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.size(Dimens.spacerSizeLarge))
            Text(
                text = title,
                style = Typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                textAlign = TextAlign.Center,
                color = BaseColors.primary
            )
            if (message.isNotEmpty()) {
                Spacer(modifier = Modifier.size(Dimens.spacerSizeExtraLarge))
                Text(
                    text = message,
                    style = Typography.labelLarge,
                    textAlign = TextAlign.Center,
                    color = BaseColors.primary
                )
            }
            if (errorMessage != null) {
                Spacer(modifier = Modifier.size(Dimens.spacerSizeMedium))
                Text(
                    text = errorMessage,
                    color = BaseColors.error,
                    style = Typography.labelMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.size(Dimens.spacerSizeExtraLarge))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.paddingMedium),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                if (isLoading.value) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(horizontal = Dimens.paddingMedium),
                        color = BaseColors.primary
                    )
                } else {
                    PrimaryButton(
                        label = positiveButtonText,
                        buttonColor = BaseColors.success,
                        onClick =
                            {
                                if (shouldDisplayLoading) {
                                    isLoading.value = true
                                }
                                onPositiveClick()
                            },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.size(Dimens.paddingMedium))
                    PrimaryButton(
                        label = negativeButtonText,
                        buttonColor = BaseColors.error,
                        onClick = onNegativeClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.size(Dimens.paddingMedium))
        }
    }
}

@Composable
@Preview
fun ConfirmationPopupPreview() {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ConfirmationPopup(
            "TEST TITLE",
            "test confirmation question",
            "yes text",
            "no text",
            onPositiveClick = {
                println("YES CLICKED")
            },
            onNegativeClick = {
                println("NO CLICKED")
            }
        )
    }
}