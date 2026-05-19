package com.example.the7wonders.ui.addGameScreen.inputPoints

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.the7wonders.R
import com.example.the7wonders.ui.base.BasePopupContainer
import com.example.the7wonders.ui.base.PrimaryButton
import com.example.the7wonders.ui.theme.BaseColors
import com.example.the7wonders.ui.theme.Dimens
import com.example.the7wonders.ui.theme.Transparency
import kotlin.math.pow

@Composable
fun GreenCardsCalculatorPopup(onApply: (points: Int) -> Unit, onDismiss: () -> Unit) {
    val cogwheelPoints = remember { mutableIntStateOf(0) }
    val tabletPoints = remember { mutableIntStateOf(0) }
    val compassPoints = remember { mutableIntStateOf(0) }
    val totalScore = remember {
        mutableIntStateOf(
            calculatePoints(
                cogwheelPoints.intValue,
                tabletPoints.intValue,
                compassPoints.intValue
            )
        )
    }
    BasePopupContainer(
        onDismiss = {
            onDismiss()
        }
    ) {
        Column() {

            Spacer(modifier = Modifier.size(Dimens.paddingLarge))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    totalScore.intValue.toString(),
                    fontSize = Dimens.extraLargeFontSize,
                    color = BaseColors.secondary.copy(Transparency.TRANSPARENCY_90)
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.padding(vertical = Dimens.paddingLarge)
            ) {
                Spacer(modifier = Modifier.size(Dimens.paddingLarge))
                PointsColumn(
                    icon = painterResource(R.drawable.gearwheel),
                    value = cogwheelPoints,
                    onUpdate = {
                        totalScore.intValue = calculatePoints(
                            cogwheelPoints.intValue,
                            tabletPoints.intValue,
                            compassPoints.intValue
                        )
                    }
                )
                Spacer(modifier = Modifier.size(Dimens.paddingLarge))
                PointsColumn(
                    icon = painterResource(R.drawable.tablet),
                    value = tabletPoints,
                    onUpdate = {
                        totalScore.intValue = calculatePoints(
                            cogwheelPoints.intValue,
                            tabletPoints.intValue,
                            compassPoints.intValue
                        )
                    }
                )
                Spacer(modifier = Modifier.size(Dimens.paddingLarge))
                PointsColumn(
                    icon = painterResource(R.drawable.compass),
                    value = compassPoints,
                    onUpdate = {
                        totalScore.intValue = calculatePoints(
                            cogwheelPoints.intValue,
                            tabletPoints.intValue,
                            compassPoints.intValue
                        )
                    }
                )
                Spacer(modifier = Modifier.size(Dimens.paddingLarge))
            }
            PrimaryButton(
                label = stringResource(R.string.apply_label),
                textColor = BaseColors.secondaryDark,
                buttonColor = BaseColors.secondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = Dimens.paddingLarge,
                        vertical = Dimens.paddingMedium
                    ),
            ) {
                onApply(totalScore.intValue)
            }
        }
    }
}

@Composable
fun PointsColumn(
    icon: Painter,
    value: MutableIntState,
    onUpdate: () -> Unit,
) {
    val width = Dimens.greenPointsInputSize
    val height = Dimens.greenPointsTextBoxSize
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(width)
    ) {
        Image(
            icon,
            contentDescription = stringResource(R.string.green_point_type_icon),
            modifier = Modifier.size(Dimens.iconSizeExtraLarge)
        )
        Spacer(modifier = Modifier.size(Dimens.paddingMedium))
        GreenPointsButton(
            icon = rememberVectorPainter(Icons.Outlined.KeyboardArrowUp),
            position = ButtonPosition.TOP,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.greenPointsButtonHeight)
        ) {
            value.intValue += 1
            onUpdate()
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(BaseColors.secondaryDark)
                .border(
                    Dimens.strokeWidthMedium,
                    BaseColors.secondary.copy(Transparency.TRANSPARENCY_70)
                )
                .padding(vertical = Dimens.paddingSmall)
                .size(width = width, height = height)
        ) {
            Text(
                value.intValue.toString(),
                fontSize = Dimens.greenPointsFontSize,
                color = BaseColors.secondary.copy(Transparency.TRANSPARENCY_90)
            )
        }
        GreenPointsButton(
            icon = rememberVectorPainter(Icons.Outlined.KeyboardArrowDown),
            position = ButtonPosition.BOTTOM,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.greenPointsButtonHeight)
        ) {

            if (value.intValue > 0) {
                value.intValue -= 1
                onUpdate()
            }
        }
    }
}

fun calculatePoints(cogwheels: Int, tablets: Int, compasses: Int): Int {
    val sets = minOf(cogwheels, tablets, compasses)
    val pointsFromSets = sets * 7
    val pointsFromCog = cogwheels.toDouble().pow(2.0).toInt()
    val pointsFromTablet = tablets.toDouble().pow(2.0).toInt()
    val pointsFromCompass = compasses.toDouble().pow(2.0).toInt()
    return pointsFromSets + pointsFromCog + pointsFromTablet + pointsFromCompass
}