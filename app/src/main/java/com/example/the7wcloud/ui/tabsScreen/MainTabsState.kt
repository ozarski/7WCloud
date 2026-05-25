package com.example.the7wcloud.ui.tabsScreen

data class MainTabsState(
    val selectedTab: MainTabs,
    val addPlayerPopupVisible: Boolean = false,
    val settingsPopupVisible: Boolean = false,
    val addPlayerError: String? = null,
    val isAdmin: Boolean = false
)
