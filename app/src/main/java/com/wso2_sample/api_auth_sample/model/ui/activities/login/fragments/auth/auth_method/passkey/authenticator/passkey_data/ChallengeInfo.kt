package com.wso2_sample.api_auth_sample.model.ui.activities.login.fragments.auth.auth_method.passkey.authenticator.passkey_data

data class ChallengeInfo (
    val requestId: String,
    val publicKeyCredentialRequestOptions: PublicKeyCredentialRequestOptions,
    val request: Any
)
