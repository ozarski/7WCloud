package com.example.the7wonders.ui

enum class Screens(val screenName: String, val route: String) {
    Login(screenName = "Login", route = "login"),
    MainTabs(screenName = "MainTabs", route = "main_tabs"),
    AddGame(screenName = "AddGame", route = "add_game"),
    GameDetails(screenName = "GameDetails", route = "game_details");

    companion object {
        const val GAME_DETAILS_ID_PARAM = "id"
    }

}