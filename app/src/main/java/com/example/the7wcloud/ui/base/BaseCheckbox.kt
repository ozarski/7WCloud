package com.example.the7wcloud.ui.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.the7wcloud.ui.theme.BaseColors
import com.example.the7wcloud.ui.theme.Dimens
import com.example.the7wcloud.ui.theme.Typography

@Composable
fun BaseCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    checkedIcon: Painter,
    uncheckedIcon: Painter,
    label: String,
    colorChecked: Color = BaseColors.primary,
    colorUnchecked: Color = BaseColors.primary,
    contentColorChecked: Color = BaseColors.textPrimary,
    contentColorUnchecked: Color = BaseColors.textSecondary,
    onClick: () -> Unit
) {
    Surface(
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(Dimens.cornerRadiusMax),
        color = if (checked) colorChecked else colorUnchecked,
        modifier = modifier
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            DefaultCheckboxIcon(
                checked,
                contentColorChecked,
                contentColorUnchecked,
                checkedIcon,
                uncheckedIcon
            )
            Text(
                label,
                style = Typography.labelMedium,
                modifier = Modifier.padding(
                    horizontal = Dimens.paddingLarge,
                    vertical = Dimens.paddingMedium
                ),
                color = if (checked) contentColorChecked else contentColorUnchecked,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
@Preview
fun BaseCheckboxPreview() {
    val checked = remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseColors.backgroundSecondary)
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BaseCheckbox(
            checked = checked.value,
            label = "checkbox test",
            modifier = Modifier.fillMaxWidth(),
            checkedIcon = rememberVectorPainter(Icons.Filled.Add),
            uncheckedIcon = rememberVectorPainter(Icons.Outlined.Clear)
        ) {
            checked.value = !checked.value
        }
    }
}