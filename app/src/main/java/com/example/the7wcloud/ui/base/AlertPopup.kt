package com.example.the7wcloud.ui.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.the7wcloud.R
import com.example.the7wcloud.ui.theme.BaseColors
import com.example.the7wcloud.ui.theme.Dimens
import com.example.the7wcloud.ui.theme.Typography

@Composable
fun AlertPopup(title: String, message: String, onDismiss: () -> Unit) {
    BasePopupContainer(
        onDismiss
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

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
                Spacer(modifier = Modifier.size(Dimens.spacerSizeExtraLarge))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.paddingMedium),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    PrimaryButton(
                        label = stringResource(R.string.ok),
                        buttonColor = BaseColors.success,
                        onClick =
                            {
                                onDismiss()
                            },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.size(Dimens.paddingMedium))
        }
    }
}