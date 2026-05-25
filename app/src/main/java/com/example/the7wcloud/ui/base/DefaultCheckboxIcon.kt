package com.example.the7wcloud.ui.base

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.example.the7wcloud.ui.theme.Dimens

@Composable
fun DefaultCheckboxIcon(
    checked: Boolean,
    colorChecked: Color,
    colorUnchecked: Color,
    iconChecked: Painter = rememberVectorPainter(Icons.Filled.Done),
    iconUnchecked: Painter = rememberVectorPainter(Icons.Filled.Clear),
) {
    Icon(
        painter = if (checked) iconChecked else iconUnchecked,
        tint = if (checked) colorChecked else colorUnchecked,
        contentDescription = "checkbox icon",
        modifier = Modifier.padding(
            horizontal = Dimens.paddingMedium,
            vertical = Dimens.paddingMedium
        ).size(Dimens.iconSizeSmall)
    )
}