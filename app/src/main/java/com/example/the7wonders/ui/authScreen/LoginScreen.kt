package com.example.the7wonders.ui.authScreen

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
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.example.the7wonders.data.remote.SupabaseConfig
import kotlinx.coroutines.launch

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
                    val credentialManager = CredentialManager.create(context)
                    val googleIdOption = GetGoogleIdOption.Builder()
                        .setServerClientId(SupabaseConfig.webClientId)
                        .setAutoSelectEnabled(false)
                        .build()
                    val request = GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build()
                    try {
                        val result = credentialManager.getCredential(context, request)
                        val credential = GoogleIdTokenCredential.createFrom(result.credential.data)
                        viewModel.signInWithGoogle(credential.idToken)
                    } catch (e: GetCredentialCancellationException) {
                        // user cancelled
                    } catch (e: Exception) {
                        viewModel.signInWithGoogle("")
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
