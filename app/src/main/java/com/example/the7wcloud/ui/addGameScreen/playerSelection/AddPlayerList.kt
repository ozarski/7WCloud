package com.example.the7wcloud.ui.addGameScreen.playerSelection

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.the7wcloud.R
import com.example.the7wcloud.ui.addGameScreen.AddGameViewModel
import com.example.the7wcloud.ui.base.BaseCheckbox
import com.example.the7wcloud.ui.theme.BaseColors
import com.example.the7wcloud.ui.theme.Dimens
import com.example.the7wcloud.ui.theme.Transparency


@Composable
fun AddPlayerList(viewModel: AddGameViewModel = hiltViewModel()) {
    val state = viewModel.state.value

    val iconsMap = mapOf<Int?, Painter>(
        1 to painterResource(R.drawable.rounded_counter_1_24),
        2 to painterResource(R.drawable.rounded_counter_2_24),
        3 to painterResource(R.drawable.rounded_counter_3_24),
        4 to painterResource(R.drawable.rounded_counter_4_24),
        5 to painterResource(R.drawable.rounded_counter_5_24),
        6 to painterResource(R.drawable.rounded_counter_6_24),
        7 to painterResource(R.drawable.rounded_counter_7_24),
    )

    val borderBrush = Brush.verticalGradient(
        listOf(
            BaseColors.onSecondary.copy(alpha = Transparency.TRANSPARENCY_30),
            BaseColors.secondary.copy(alpha = Transparency.TRANSPARENCY_10),
        )
    )

    Card(
        modifier = Modifier
            .heightIn(max = Dimens.addPlayerListMaxHeight)
            .width(Dimens.addPlayerListWidth),
        border = BorderStroke(Dimens.strokeWidthMedium, borderBrush),
        shape = RoundedCornerShape(Dimens.cornerRadiusExtraLarge),
        colors = CardDefaults.cardColors(
            containerColor = BaseColors.primary.copy(alpha = Transparency.TRANSPARENCY_10)
        )
    ) {
        LazyColumn(modifier = Modifier.padding(horizontal = Dimens.paddingLarge)) {
            item { Spacer(modifier = Modifier.size(Dimens.paddingLarge)) }
            items(state.availablePlayers.size) { index ->
                val player = state.availablePlayers[index]
                Crossfade(
                    targetState = player
                ) { player ->
                    BaseCheckbox(
                        modifier = Modifier.fillMaxWidth(),
                        checked = player.isPlaying,
                        checkedIcon = iconsMap.getOrDefault(
                            player.ordinal,
                            rememberVectorPainter(Icons.Outlined.Close)
                        ),
                        uncheckedIcon = rememberVectorPainter(Icons.Outlined.Close),
                        label = player.name,
                        colorUnchecked = BaseColors.secondaryDark.copy(alpha = Transparency.TRANSPARENCY_30),
                        colorChecked = BaseColors.secondary.copy(alpha = Transparency.TRANSPARENCY_70),
                        contentColorUnchecked = BaseColors.textSecondary.copy(alpha = Transparency.TRANSPARENCY_50),
                        contentColorChecked = BaseColors.textPrimary
                    ) {
                        viewModel.togglePlayer(index)
                    }
                }
            }
            item { Spacer(modifier = Modifier.size(Dimens.paddingLarge)) }
        }
    }
}


//val viewModel = AddGameViewModel()
//
//@Composable
//@Preview
//fun AddPlayerListPreview() {
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        AddPlayerList(viewModel = viewModel)
//    }
//}