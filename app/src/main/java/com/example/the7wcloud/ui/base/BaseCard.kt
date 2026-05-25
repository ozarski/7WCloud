package com.example.the7wcloud.ui.base

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.the7wcloud.ui.theme.BaseColors
import com.example.the7wcloud.ui.theme.Dimens
import com.example.the7wcloud.ui.theme.Transparency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onHold: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    CompositionLocalProvider(
        LocalRippleConfiguration provides RippleConfiguration(color = BaseColors.secondaryDark)
    ) {
        val borderBrush = Brush.horizontalGradient(
            listOf(
                BaseColors.secondaryDark.copy(alpha = Transparency.TRANSPARENCY_10),
                BaseColors.secondary.copy(alpha = Transparency.TRANSPARENCY_10),
            )
        )
        Card(
            modifier = modifier
                .combinedClickable(
                    interactionSource = interactionSource,
                    onLongClick = {
                        onHold()
                    },
                    onClick = onClick,
                    indication = null
                )
                .clip(shape = RoundedCornerShape(Dimens.cornerRadiusExtraLarge))
                .indication(
                    interactionSource = interactionSource,
                    indication = remember { ripple() }
                ),
            shape = RoundedCornerShape(Dimens.cornerRadiusExtraLarge),
            colors = CardDefaults.cardColors(containerColor = BaseColors.primary.copy(alpha = Transparency.TRANSPARENCY_30)),
            border = BorderStroke(
                Dimens.strokeWidthMedium,
                //BaseColors.secondary.copy(alpha = Transparency.TRANSPARENCY_10)
                borderBrush
            ),
        ) {
            Box(modifier = Modifier.padding(Dimens.paddingLarge)) {
                content()
            }
        }
    }
}

@Preview
@Composable
fun BaseCardPreview() {
    BaseCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        onClick = { println("card clicked") }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "CARD TEST CONTENT",
                textAlign = TextAlign.Center
            )
        }
    }
}