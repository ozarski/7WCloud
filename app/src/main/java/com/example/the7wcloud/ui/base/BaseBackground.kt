package com.example.the7wcloud.ui.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import com.example.the7wcloud.ui.theme.BaseColors
import com.example.the7wcloud.ui.theme.Typography

enum class BackgroundOrientation {
    Vertical,
    Horizontal
}

@Composable
fun BaseBackground(
    modifier: Modifier = Modifier,
    flip: Boolean = false,
    orientation: BackgroundOrientation = BackgroundOrientation.Vertical,
    content: @Composable () -> Unit
) {

    val colors = listOf(
        BaseColors.onSecondary,
        BaseColors.secondaryDark,
    )

    val brush =
        if (orientation == BackgroundOrientation.Horizontal) {
            Brush.horizontalGradient(if (!flip) colors else colors.reversed())
        } else {
            Brush.verticalGradient(if (!flip) colors else colors.reversed())
        }

    Box(
        modifier = modifier
            .background(brush)
            .systemBarsPadding(),
    ) {
        content()
    }
}

@Composable
@Preview
fun BaseBackgroundPreview() {
    BaseBackground(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("TEST BACKGROUND CONTENT", style = Typography.titleLarge)
        }
    }
}