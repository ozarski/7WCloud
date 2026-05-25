package com.example.the7wcloud.ui.tabsScreen.gamesTab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.the7wcloud.R
import com.example.the7wcloud.domain.model.GameModel
import com.example.the7wcloud.ui.base.BaseCard
import com.example.the7wcloud.ui.theme.BaseColors
import com.example.the7wcloud.ui.theme.Dimens
import com.example.the7wcloud.ui.theme.Typography
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@Composable
fun GameListItem(
    modifier: Modifier = Modifier,
    game: GameModel,
    onClick: (Long?) -> Unit,
    onHold: (GameModel?) -> Unit
) {

    val leaderboard = game.playerScores.sortedByDescending { it.second }

    val format = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ROOT)
    val dateFormatted =
        Calendar.getInstance().apply { timeInMillis = game.date }.toInstant()
            .atZone(ZoneId.systemDefault()).format(format)
    BaseCard(
        onClick = {
            onClick(game.id)
        },
        onHold = {
            onHold(game)
        },
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(leaderboard, dateFormatted)
            Spacer(modifier = Modifier.size(Dimens.paddingLarge))
            PlayerList(leaderboard)
            Spacer(modifier = Modifier.size(Dimens.paddingSmall))
        }
    }
}

@Composable
fun PlayerList(leaderboard: List<Pair<String, Int>>) {
    leaderboard.subList(1, leaderboard.size).forEachIndexed { index, playerScore ->
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row {
                Text(
                    "${index + 2}.",
                    style = Typography.labelLarge.copy(fontFamily = FontFamily.Monospace),
                    color = BaseColors.secondary
                )
                Spacer(modifier = Modifier.size(Dimens.paddingSmall))
                Text(
                    playerScore.first,
                    style = Typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.widthIn(max = Dimens.gameListItemPlayerNameMaxWidth)
                )
            }
            Text("${playerScore.second} pts", style = Typography.labelLarge)
        }
        if (index != leaderboard.size - 2) Spacer(modifier = Modifier.size(Dimens.paddingMedium))
    }
}

@Composable
fun TopBar(leaderboard: List<Pair<String, Int>>, dateFormatted: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(R.drawable.rounded_crown_24),
                "winner icon",
                tint = BaseColors.winIconColor,
                modifier = Modifier.size(Dimens.iconSizeLarge)
            )
            Spacer(modifier = Modifier.size(Dimens.paddingMedium))
            Column {
                Text(
                    leaderboard.first().first,
                    style = Typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.widthIn(max = Dimens.defaultTextViewMaxWidth)
                )
                Text(
                    "${leaderboard.first().second} points",
                    style = Typography.bodyLarge
                )
            }
        }
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(top = Dimens.paddingSmall)
        ) {
            Icon(
                Icons.Outlined.DateRange,
                "game date icon",
                modifier = Modifier.size(Dimens.iconSizeSmall),
                tint = BaseColors.secondary
            )
            Spacer(modifier = Modifier.size(Dimens.paddingMedium))
            Text(
                dateFormatted,
                style = Typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                color = BaseColors.secondary
            )
        }
    }
}

@Composable
@Preview
fun GameListItemPreview() {
    val playerScores = listOf(
        Pair("Player one", 12),
        Pair("Player two", 23),
        Pair("Player three", 34),
        Pair("Player four", 45),
        Pair("Player five", 56),
        Pair("Player six", 67),
    )
    val gameModel = GameModel(
        id = 1,
        date = Calendar.getInstance().timeInMillis,
        playerScores = playerScores
    )

    Column(modifier = Modifier.padding(Dimens.paddingMedium)) {
        GameListItem(game = gameModel, onClick = {}, onHold = {})
    }
}