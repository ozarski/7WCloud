package com.example.the7wonders.ui.util

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun mapToUserMessage(e: Throwable): String = when (e) {
    is UnknownHostException, is ConnectException -> "Connection error"
    is SocketTimeoutException -> "Connection timed out"
    else -> e.message ?: "An unexpected error occurred"
}
