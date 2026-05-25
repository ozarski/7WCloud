package com.example.the7wcloud.ui.base

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.the7wcloud.ui.theme.BaseColors
import com.example.the7wcloud.ui.theme.Dimens
import com.example.the7wcloud.ui.theme.Transparency
import com.example.the7wcloud.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    label: String,
    buttonColor: Color = BaseColors.onPrimary,
    textColor: Color = BaseColors.primary,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val borderBrush = Brush.radialGradient(
        listOf(
            BaseColors.onSecondary.copy(alpha = Transparency.TRANSPARENCY_10),
            BaseColors.secondaryDark.copy(alpha = Transparency.TRANSPARENCY_10),
        )
    )
    CompositionLocalProvider(
        LocalRippleConfiguration provides RippleConfiguration(color = BaseColors.secondaryDark)
    ) {
        ElevatedButton(
            modifier = modifier,
            onClick = onClick,
            elevation = ButtonDefaults.buttonElevation(defaultElevation = Dimens.elevationExtraSmall),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = buttonColor.copy(alpha = Transparency.TRANSPARENCY_70),
                contentColor = textColor,
            ),
            border = BorderStroke(Dimens.strokeWidthMedium, borderBrush),
            enabled = enabled
        ) {
            Text(label, style = Typography.labelMedium)
        }
    }
}

@Preview
@Composable
fun PrimaryButtonPreview() {
    PrimaryButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        label = "Primary Button Test",
        onClick = {
            println("clicked")
        }
    )
}