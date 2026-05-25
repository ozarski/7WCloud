package com.example.the7wcloud.di

import com.example.the7wcloud.data.remote.SupabaseConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = SupabaseConfig.url,
            supabaseKey = SupabaseConfig.anonKey
        ) {
            install(Auth)
            install(Postgrest) {
                serializer = KotlinXSerializer(Json { ignoreUnknownKeys = true })
            }
        }
    }
}
