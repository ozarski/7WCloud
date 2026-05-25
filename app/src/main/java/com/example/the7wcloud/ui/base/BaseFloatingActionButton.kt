package com.example.the7wcloud.ui.base

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.the7wcloud.ui.theme.BaseColors
import com.example.the7wcloud.ui.theme.Dimens

@Composable
fun BaseFloatingActionButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    color: Color = BaseColors.onSecondary,
    iconColor: Color = BaseColors.primary,
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = color,
        contentColor = iconColor,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = Dimens.elevationLarge
        ),
        shape = CircleShape,
        interactionSource = interactionSource
    ) {
        Icon(icon, "FAB icon")
    }
}

@Preview
@Composable
fun BaseFloatingActionButtonPreview() {
    val interactionSource = remember { MutableInteractionSource() }
    BaseFloatingActionButton(icon = Icons.Outlined.Add, modifier = Modifier.padding(16.dp), interactionSource = interactionSource) {
        println("FAB clicked")
    }
}
