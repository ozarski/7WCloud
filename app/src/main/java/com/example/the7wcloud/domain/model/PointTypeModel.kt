package com.example.the7wcloud.domain.model

import androidx.compose.ui.graphics.Color
import com.example.the7wcloud.R
import com.example.the7wcloud.ui.theme.PointTypeColors


interface PointTypeInterface {
    val pointName: String
    val color: Color
    val icon: Int

    companion object {
        fun getPlayerResultsIndex(pointType: PointTypeInterface): Int{
            when(pointType){
                is BasePointTypes -> {
                    val index = BasePointTypes.entries.indexOf(pointType)
                    return index
                }
                is ArmadaPointTypes -> {
                    val index = ArmadaPointTypes.entries.indexOf(pointType)
                    return index + BasePointTypes.entries.size
                }
                is CityPointTypes -> {
                    val index = CityPointTypes.entries.indexOf(pointType)
                    return index + BasePointTypes.entries.size + ArmadaPointTypes.entries.size
                }
                is LeaderPointTypes -> {
                    val index = LeaderPointTypes.entries.indexOf(pointType)
                    return index + BasePointTypes.entries.size + ArmadaPointTypes.entries.size + CityPointTypes.entries.size
                }
                else -> {
                    return -1
                }
            }
        }
    }
}

enum class BasePointTypes(override val pointName: String, override val color: Color, override val icon: Int): PointTypeInterface {
    Wonder("Wonder", PointTypeColors.wonder, R.drawable.mingcute_egyptian_pyramids),
    Military("Military", PointTypeColors.military, R.drawable.rounded_swords_24),
    Gold("Gold", PointTypeColors.gold, R.drawable.rounded_money_bag_24),
    Blue("Blue card", PointTypeColors.blue, R.drawable.rounded_theater_comedy_24),
    Yellow("Yellow card", PointTypeColors.yellow, R.drawable.hugeicons_camel),
    Green("Green card", PointTypeColors.green, R.drawable.rounded_architecture_24),
    Purple("Purple card", PointTypeColors.purple, R.drawable.pop_hammer_sledge)
}

enum class ArmadaPointTypes(override val pointName: String, override val color: Color, override val icon: Int): PointTypeInterface {
    NavalConflicts("Naval conflicts", PointTypeColors.navalConflicts, R.drawable.naval_conflict_icon),
    IslandCards("Island cards", PointTypeColors.island, R.drawable.island),
    NavalVictory("Naval victory", PointTypeColors.navalVictory, R.drawable.boat)
}

enum class CityPointTypes(override val pointName: String, override val color: Color, override val icon: Int): PointTypeInterface {
    CityCards("City cards", PointTypeColors.city, R.drawable.city)
}

enum class LeaderPointTypes(override val pointName: String, override val color: Color, override val icon: Int): PointTypeInterface {
    LeaderCards("Leader cards", PointTypeColors.leader, R.drawable.queen)
}