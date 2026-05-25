package com.example.the7wcloud.ui.tabsScreen.playersTab

import android.icu.text.DecimalFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.the7wcloud.R
import com.example.the7wcloud.domain.model.PlayerModel
import com.example.the7wcloud.ui.base.BaseCard
import com.example.the7wcloud.ui.theme.BaseColors
import com.example.the7wcloud.ui.theme.Dimens
import com.example.the7wcloud.ui.theme.Typography

@Composable
fun PlayerListItem(player: PlayerModel, onClick: (Long?) -> Unit, onHold: (PlayerModel?) -> Unit) {
    BaseCard(
        modifier = Modifier.padding(Dimens.paddingMedium),
        onClick = {
            onClick(player.id)
        },
        onHold = {
            onHold(player)
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            IconInfoRow(
                imageVector = Icons.Outlined.Person,
                text = player.name,
                modifier = Modifier.fillMaxWidth(),
                textStyle = Typography.titleMedium,
            )
            Spacer(modifier = Modifier.size(Dimens.paddingMedium))
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconInfoRow(
                        painter = painterResource(R.drawable.rounded_crown_24),
                        text = if (player.wins == null) "0" else player.wins.toString(),
                        iconColor = BaseColors.winIconColor
                    )
                    Spacer(modifier = Modifier.size(Dimens.paddingExtraLarge))
                    IconInfoRow(
                        painter = painterResource(R.drawable.rounded_numbers_24),
                        text = if (player.games == null) "0" else player.games.toString(),
                    )
                }
            }
            Spacer(modifier = Modifier.size(Dimens.paddingMedium))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    stringResource(R.string.top_score),
                    style = Typography.labelLarge,
                    color = BaseColors.secondary
                )
                Text(
                    if (player.topScore == null) "-" else player.topScore.toString(),
                    style = Typography.labelLarge
                )
            }
            Spacer(modifier = Modifier.size(Dimens.paddingSmall))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    stringResource(R.string.avg_place),
                    style = Typography.labelLarge,
                    color = BaseColors.secondary
                )
                Spacer(modifier = Modifier.size(Dimens.paddingExtraLarge))
                Text(
                    if (player.avgPlacement == null) "-" else DecimalFormat("#.#").format(player.avgPlacement)
                        .toString(),
                    style = Typography.labelLarge,
                )
            }
        }
    }
}

@Composable
fun IconInfoRow(
    modifier: Modifier = Modifier,
    painter: Painter? = null,
    imageVector: ImageVector? = null,
    text: String,
    textStyle: TextStyle = Typography.labelLarge,
    iconColor: Color = BaseColors.textPrimary
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        if (painter != null) {
            Icon(
                painter,
                "info row icon",
                modifier = Modifier.size(textStyle.lineHeight.value.dp),
                tint = iconColor
            )
        } else if (imageVector != null) {
            Icon(
                imageVector,
                "info row icon",
                modifier = Modifier.size(textStyle.lineHeight.value.dp),
                tint = iconColor
            )
        }
        Spacer(modifier = Modifier.size(Dimens.paddingSmall))
        Text(text, style = textStyle, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
@Preview
fun PlayerListItemPreview() {
    val player = PlayerModel(
        id = 1,
        name = "Wojtek",
        wins = 10,
        games = 50,
        topScore = 60,
        avgPlacement = 3.77
    )
    Column {
        PlayerListItem(player, onHold = {}, onClick = {})
    }
}