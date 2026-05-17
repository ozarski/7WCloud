package com.example.the7wonders.data.remote

import com.example.the7wonders.BuildConfig

object SupabaseConfig {
    val url: String = BuildConfig.SUPABASE_URL
    val anonKey: String = BuildConfig.SUPABASE_ANON_KEY
    val webClientId: String = BuildConfig.WEB_CLIENT_ID
}
