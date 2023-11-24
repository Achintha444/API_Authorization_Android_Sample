package com.example.api_auth_sample.model.api.app_auth_manager

class TokenRequestCallback(
    private val onSuccess: (accessToken: String) -> Unit,
    private val onFailure: (error: Exception) -> Unit
) {

    fun onTokenReceived(accessToken: String) {
        onSuccess.invoke(accessToken)
    }

    fun onTokenRequestFailed(error: Exception) {
        onFailure.invoke(error)
    }
}
