package com.example.the7wcloud.ui.addGameScreen.inputPoints

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import com.example.the7wcloud.ui.theme.BaseColors
import com.example.the7wcloud.ui.theme.Dimens
import com.example.the7wcloud.ui.theme.Transparency

enum class ButtonPosition {
    BOTTOM,
    TOP
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GreenPointsButton(
    modifier: Modifier = Modifier,
    icon: Painter,
    enabled: Boolean = true,
    position: ButtonPosition = ButtonPosition.BOTTOM,
    onClick: () -> Unit
) {
    val borderBrush = Brush.radialGradient(
        listOf(
            BaseColors.onSecondary.copy(alpha = Transparency.TRANSPARENCY_10),
            BaseColors.secondaryDark.copy(alpha = Transparency.TRANSPARENCY_10),
        )
    )
    val shape = when (position) {
        ButtonPosition.BOTTOM -> RoundedCornerShape(
            bottomStart = Dimens.cornerRadiusMedium,
            bottomEnd = Dimens.cornerRadiusMedium
        )

        ButtonPosition.TOP -> RoundedCornerShape(
            topStart = Dimens.cornerRadiusMedium,
            topEnd = Dimens.cornerRadiusMedium
        )
    }
    CompositionLocalProvider(
        LocalRippleConfiguration provides RippleConfiguration(color = BaseColors.secondaryDark)
    ) {
        ElevatedButton(
            modifier = modifier,
            onClick = onClick,
            shape = shape,
            elevation = ButtonDefaults.buttonElevation(defaultElevation = Dimens.elevationExtraSmall),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = BaseColors.secondary.copy(alpha = Transparency.TRANSPARENCY_70),
                contentColor = BaseColors.onSecondary,
            ),
            enabled = enabled
        ) {
            Icon(
                icon,
                "green points button icon",
                tint = BaseColors.secondaryDark,
                modifier = Modifier.size(Dimens.iconSizeLarge)
            )
        }
    }
}
