package com.example.the7wonders.ui.adminScreen

import com.example.the7wonders.domain.model.AdminUserModel

data class AdminPanelState(
    val isLoading: Boolean = false,
    val users: List<AdminUserModel> = emptyList(),
    val error: String? = null,
    val actionError: String? = null,
    val currentUserId: String? = null,
    val actionLoadingUserId: String? = null
)
