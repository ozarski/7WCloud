package com.example.the7wcloud.ui.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.the7wcloud.ui.theme.BaseColors
import com.example.the7wcloud.ui.theme.Dimens
import com.example.the7wcloud.ui.theme.Typography

@Composable
fun ErrorWidget(
    message: String?,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(Dimens.paddingExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Outlined.ErrorOutline,
            contentDescription = null,
            modifier = Modifier.size(Dimens.iconSizeExtraLarge),
            tint = BaseColors.winIconColor
        )
        Spacer(modifier = Modifier.size(Dimens.paddingLarge))
        Text(
            text = message ?: "An unexpected error occurred",
            style = Typography.bodyLarge,
            color = BaseColors.textSecondary,
            textAlign = TextAlign.Center
        )
        if (onRetry != null) {
            Spacer(modifier = Modifier.size(Dimens.paddingLarge))
            PrimaryButton(
                label = "Retry",
                buttonColor = BaseColors.secondary,
                textColor = BaseColors.secondaryDark,
            ) {
                onRetry()
            }
        }
    }
}
