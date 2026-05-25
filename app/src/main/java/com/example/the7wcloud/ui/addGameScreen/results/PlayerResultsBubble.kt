package com.example.the7wcloud.ui.addGameScreen.results

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.the7wcloud.R
import com.example.the7wcloud.domain.model.PlayerResultModel
import com.example.the7wcloud.ui.theme.BaseColors
import com.example.the7wcloud.ui.theme.Dimens
import com.example.the7wcloud.ui.theme.Transparency
import com.example.the7wcloud.ui.theme.Typography

@Composable
fun PlayerResultsBubble(
    modifier: Modifier = Modifier,
    results: PlayerResultModel,
    iconID: Int,
    color: Color
) {
    Box(
        modifier = modifier
            .background(
                shape = CircleShape,
                color = BaseColors.secondaryDark.copy(alpha = Transparency.TRANSPARENCY_50)
            )
            .border(width = Dimens.strokeWidthLarge, color = color, shape = CircleShape)
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                layout(placeable.width, placeable.width) {
                    placeable.placeRelative(0, 0)
                }
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(
                    start = Dimens.paddingExtraLarge,
                    end = Dimens.paddingExtraLarge,
                    top = Dimens.paddingLarge
                )
                .width(Dimens.resultBubbleWidth)
        ) {
            Icon(
                painterResource(iconID),
                "player result icon",
                modifier = Modifier.size(Dimens.resultBubbleIconSize),
                tint = color
            )
            Text(
                results.playerName,
                style = Typography.labelLarge,
                color = BaseColors.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                "${results.totalScore} pts",
                style = Typography.bodyLarge,
                color = color
            )
        }
    }
}

@Composable
fun EmptyBubble(
    modifier: Modifier = Modifier,
    iconID: Int,
    color: Color
) {
    Box(
        modifier = modifier
            .shadow(elevation = Dimens.elevationSmall, shape = CircleShape)
            .background(shape = CircleShape, color = BaseColors.primary)
            .border(width = Dimens.strokeWidthLarge, color = color, shape = CircleShape)
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                layout(placeable.width, placeable.width) {
                    placeable.placeRelative(0, 0)
                }
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(
                    start = Dimens.paddingExtraLarge,
                    end = Dimens.paddingExtraLarge,
                    top = Dimens.paddingLarge
                )
                .width(Dimens.resultBubbleWidth)
        ) {
            Icon(
                painterResource(iconID),
                "player result icon",
                modifier = Modifier.size(Dimens.resultBubbleIconSize),
                tint = color
            )
            Text(
                "-",
                style = Typography.labelLarge,
                color = BaseColors.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                "- pts",
                style = Typography.bodyLarge,
                color = color
            )
        }
    }
}

@Composable
@Preview
fun PlayerResultsBubblePreview() {
    val results = PlayerResultModel(
        playerID = 1,
        playerName = "Player One",
        totalScore = 60,
        placement = 1,
        scores = emptyList()
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        PlayerResultsBubble(
            results = results,
            iconID = R.drawable.rounded_workspace_premium_24,
            color = BaseColors.thirdPlaceColor
        )
    }
}