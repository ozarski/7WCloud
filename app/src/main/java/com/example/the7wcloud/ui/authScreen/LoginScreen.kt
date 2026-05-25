package com.example.the7wcloud.ui.authScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.example.the7wcloud.R
import com.example.the7wcloud.data.remote.SupabaseConfig
import com.example.the7wcloud.ui.base.BackgroundOrientation
import com.example.the7wcloud.ui.base.BaseBackground
import com.example.the7wcloud.ui.base.LoadingScreen
import com.example.the7wcloud.ui.base.PrimaryButton
import com.example.the7wcloud.ui.theme.BaseColors
import com.example.the7wcloud.ui.theme.Dimens
import com.example.the7wcloud.ui.theme.Transparency
import com.example.the7wcloud.ui.theme.Typography
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

private const val TAG = "LoginScreen"

@Composable
fun LoginScreen(viewModel: AuthViewModel) {
    val state = viewModel.state.value
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    BaseBackground(
        modifier = Modifier.fillMaxSize(),
        orientation = BackgroundOrientation.Horizontal,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.paddingExtraLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.mingcute_egyptian_pyramids),
                contentDescription = null,
                modifier = Modifier.size(Dimens.iconSizeLoginScreen),
                tint = BaseColors.secondary.copy(alpha = Transparency.TRANSPARENCY_50),
            )

            Spacer(modifier = Modifier.size(Dimens.spacerSizeMedium))

            Text(
                text = "7 Wonders",
                style = Typography.titleLarge,
                color = BaseColors.primary,
            )

            Spacer(modifier = Modifier.size(Dimens.spacerSizeSmall))

            Text(
                text = "Score Tracker",
                style = Typography.labelLarge,
                color = BaseColors.secondary,
            )

            Spacer(modifier = Modifier.size(Dimens.spacerSizeExtraLarge))

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                label = "Sign in with Google",
                buttonColor = BaseColors.secondary,
                textColor = BaseColors.textPrimary,
                onClick = {
                    scope.launch {
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
                            val result = credentialManager.getCredential(context, request)
                            val credential = GoogleIdTokenCredential.createFrom(result.credential.data)
                            viewModel.signInWithGoogle(credential.idToken)
                        } catch (_: GetCredentialCancellationException) {
                            viewModel.setError("Sign-in cancelled!")
                        } catch (_: GetCredentialException) {
                            viewModel.setError("Credential error!")
                        } catch (_: Exception) {
                            viewModel.setError("Google sign-in failed!")
                        }
                    }
                },
                enabled = !state.isLoading,
            )

            if (state.isLoading) {
                Spacer(modifier = Modifier.size(Dimens.spacerSizeMedium))
                LoadingScreen(indicatorColor = BaseColors.secondary)
            }

            state.error?.let { error ->
                Spacer(modifier = Modifier.size(Dimens.spacerSizeMedium))
                Text(
                    text = error,
                    style = Typography.labelLarge,
                    color = BaseColors.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
