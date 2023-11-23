package com.example.api_auth_sample.api.app_auth_manager

import com.example.api_auth_sample.api.cutom_trust_client.CustomTrustHttpURLConnection
import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.api_auth_sample.api.cutom_trust_client.CustomTrust
import com.example.api_auth_sample.util.config.Configuration
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.TokenRequest
import okhttp3.OkHttpClient
import javax.net.ssl.X509TrustManager

class AppAuthManager(private val context: Context) {

    private val clientId: String = Configuration.getInstance(context).clientId
    private val redirectUri: Uri = Configuration.getInstance(context).redirectUri
    private val tokenEndpoint: Uri = Configuration.getInstance(context).tokenUri
    private val authorizeEndpoint: Uri = Configuration.getInstance(context).authorizeUri
    private val serviceConfig: AuthorizationServiceConfiguration =
        AuthorizationServiceConfiguration(
            Uri.parse(authorizeEndpoint.toString()),  // Authorization endpoint
            Uri.parse(tokenEndpoint.toString()) // Token endpoint
        )

    companion object {
        private const val TAG = "com.example.api_auth_sample.api.app_auth_manager.AppAuthManager"
    }

    private val customTrustClient: OkHttpClient = CustomTrust.getInstance(context).client

    fun exchangeAuthorizationCodeForAccessToken(authorizationCode: String, callback: TokenRequestCallback) {
        val tokenRequest: TokenRequest = TokenRequest.Builder(
            serviceConfig,
            clientId
        )
            .setAuthorizationCode(authorizationCode)
            .setClientId(clientId)
            .setRedirectUri(redirectUri)
            .build()
        val authService: AuthorizationService = AuthorizationService(
            context,
            AppAuthConfiguration.Builder()
                .setConnectionBuilder { url ->
                    CustomTrustHttpURLConnection(
                        url,
                        customTrustClient.x509TrustManager as X509TrustManager,
                        customTrustClient.sslSocketFactory
                    ).getConnection()
                }
                .build()
        )

        try {
            authService.performTokenRequest(tokenRequest) { response, ex ->
                if (response != null) {
                    // Access token obtained successfully
                    val accessToken: String = response.accessToken!!
                    Log.d(TAG, "Access Token: $accessToken")
                    // Invoke the callback with the access token
                    callback.onTokenReceived(accessToken)
                } else {
                    // Token request failed
                    Log.e(TAG, "Token request failed: $ex")
                    // Invoke the callback with the error
                    callback.onTokenRequestFailed(ex ?: RuntimeException("Token request failed"))
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Token request failed: $ex")
            // Invoke the callback with the error
            callback.onTokenRequestFailed(ex)
        } finally {
            authService.dispose()
        }
    }
}