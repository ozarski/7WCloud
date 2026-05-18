package com.example.the7wonders.ui.authScreen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.example.the7wonders.data.remote.SupabaseConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

private const val TAG = "LoginScreen"

@Composable
fun LoginScreen(viewModel: AuthViewModel) {
    val state = viewModel.state.value
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("7 Wonders Score Tracker")

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                scope.launch {
                    Log.d(TAG, "Sign-in button clicked")
                    val credentialManager = CredentialManager.create(context)
                    val googleIdOption = GetGoogleIdOption.Builder()
                        .setServerClientId(SupabaseConfig.webClientId)
                        .setAutoSelectEnabled(false)
                        .setFilterByAuthorizedAccounts(false)
                        .build()
                    val request = GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build()
                    try {
                        Log.d(TAG, "Launching credential manager")
                        val result = credentialManager.getCredential(context, request)
                        Log.d(TAG, "Credential manager returned result: type=${result.credential.type}")
                        val credential = GoogleIdTokenCredential.createFrom(result.credential.data)
                        Log.i(TAG, "Google credential obtained: idToken length=${credential.idToken.length}")
                        viewModel.signInWithGoogle(credential.idToken)
                    } catch (e: GetCredentialCancellationException) {
                        Log.d(TAG, "User cancelled credential selection")
                    } catch (e: GetCredentialException) {
                        Log.e(TAG, "Credential Manager error: type=${e.type}", e)
                        viewModel.setError("Credential error: ${e.message}")
                    } catch (e: Exception) {
                        Log.e(TAG, "Unexpected error during sign-in", e)
                        viewModel.setError("Google sign-in failed: ${e.message}")
                    }
                }
            },
            enabled = !state.isLoading
        ) {
            Text("Sign in with Google")
        }

        if (state.isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }

        state.error?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Text("Error: $error")
        }
    }
}
