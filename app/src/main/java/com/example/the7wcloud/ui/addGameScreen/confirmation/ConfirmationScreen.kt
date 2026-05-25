package com.example.the7wcloud.ui.addGameScreen.confirmation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.the7wcloud.domain.model.BasePointTypes
import com.example.the7wcloud.domain.model.PlayerResultModel
import com.example.the7wcloud.domain.model.PointTypeInterface
import com.example.the7wcloud.ui.addGameScreen.AddGameViewModel
import com.example.the7wcloud.ui.base.BaseCard
import com.example.the7wcloud.ui.base.PrimaryButton
import com.example.the7wcloud.ui.gameDetailsScreen.ScoreGridItem
import com.example.the7wcloud.ui.gameDetailsScreen.getViableScores
import com.example.the7wcloud.ui.theme.BaseColors
import com.example.the7wcloud.ui.theme.Dimens
import com.example.the7wcloud.ui.theme.Transparency
import com.example.the7wcloud.ui.theme.Typography
import kotlin.math.min

@Composable
fun ConfirmationScreen(viewModel: AddGameViewModel = hiltViewModel()) {
    val state = viewModel.state.value

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Dimens.paddingExtraLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.size(Dimens.spacerSizeLarge))

            Text(
                text = "Review Scores",
                style = Typography.titleLarge,
                color = BaseColors.primary,
            )

            Spacer(modifier = Modifier.size(Dimens.spacerSizeMedium))

            state.results.sortedBy { it.playerName }.forEach { result ->
                PlayerScoreCard(result)
                Spacer(modifier = Modifier.size(Dimens.spacerSizeMedium))
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.paddingExtraLarge),
            horizontalArrangement = Arrangement.spacedBy(Dimens.spacerSizeMedium),
        ) {
            PrimaryButton(
                modifier = Modifier.weight(1f),
                label = "Back",
                onClick = { viewModel.backToPointInput() },
            )

            PrimaryButton(
                modifier = Modifier.weight(1f),
                label = "Looks good!",
                buttonColor = BaseColors.success,
                textColor = BaseColors.primary,
                onClick = { viewModel.submitGame() },
            )
        }
    }
}

@Composable
private fun PlayerScoreCard(result: PlayerResultModel) {
    val scores = getViableScores(result.scores)

    BaseCard {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = result.playerName,
                style = Typography.labelLarge,
                color = BaseColors.primary,
            )

            Spacer(modifier = Modifier.size(Dimens.spacerSizeMedium))

            ResultsRow(
                items = scores.subList(0, minOf(3, scores.size))
            )

            if (scores.size > 3) {
                ResultsRow(
                    items = scores.subList(3, min(6, scores.size))
                )
            }

            if (scores.size > 6) {
                ResultsRow(
                    items = scores.subList(6, min(9, scores.size))
                )
            }
            if (scores.size > 9) {
                ResultsRow(
                    items = scores.subList(9, scores.size)
                )
            }
            Spacer(modifier = Modifier.size(Dimens.spacerSizeMedium))
        }
    }
}

@Composable
fun ResultsRow(items: List<Pair<PointTypeInterface, Int?>>) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEach { score ->
            if (score.second != null) {
                ScoreGridItem(
                    score
                )
            } else {
                ScoreGridItem(
                    Pair(first = BasePointTypes.Wonder, second = null),
                    modifier = Modifier.alpha(Transparency.TRANSPARENCY_0)
                )
            }
        }
    }
}
