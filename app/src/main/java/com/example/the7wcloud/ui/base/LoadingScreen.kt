package com.example.the7wcloud.ui.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.the7wcloud.ui.theme.BaseColors

@Composable
fun LoadingScreen(modifier: Modifier = Modifier, indicatorColor: Color = BaseColors.secondary) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        CircularProgressIndicator(color = indicatorColor)
    }
}

@Composable
@Preview
fun LoadingScreenPreview() {
    LoadingScreen(modifier = Modifier.fillMaxSize())
}