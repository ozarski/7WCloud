package com.example.the7wcloud.ui.base

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.the7wcloud.ui.theme.BaseColors
import com.example.the7wcloud.ui.theme.Dimens
import com.example.the7wcloud.ui.theme.Transparency

@Composable
fun BasePopupContainer(onDismiss: () -> Unit, popupBody: @Composable () -> Unit) {

    val borderBrush = Brush.horizontalGradient(
        listOf(
            BaseColors.onSecondary.copy(alpha = Transparency.TRANSPARENCY_10),
            BaseColors.secondary.copy(alpha = Transparency.TRANSPARENCY_10),
        )
    )
    Dialog(onDismissRequest = onDismiss) {
        Card(
            border = BorderStroke(
                Dimens.strokeWidthMedium,
                borderBrush
            ),
            colors = CardDefaults.cardColors(
                containerColor = BaseColors.secondaryDark.copy(alpha = Transparency.TRANSPARENCY_90)
            ),
            shape = RoundedCornerShape(Dimens.cornerRadiusExtraLarge)
        ) {
            Box(modifier = Modifier.padding(Dimens.paddingMedium)) {
                popupBody()
            }
        }
    }
}

@Composable
@Preview
fun BasePopupContainerPreview() {
    BasePopupContainer(onDismiss = {}) {
        Column(
            modifier = Modifier
                .width(300.dp)
                .height(300.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("base popup container test")
        }
    }
}