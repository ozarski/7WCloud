package com.example.the7wcloud.ui.util

import io.github.jan.supabase.exceptions.UnauthorizedRestException
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun mapToUserMessage(e: Throwable): String = when (e) {
    is PostgrestRestException -> when (e.code) {
        "42501" -> "You don't have permission to perform this action"
        "23505" -> "A player with this name already exists"
        else -> e.error
    }
    is UnauthorizedRestException -> "You don't have permission to perform this action"
    is UnknownHostException, is ConnectException -> "Connection error"
    is SocketTimeoutException -> "Connection timed out"
    else -> e.message ?: "An unexpected error occurred"
}
