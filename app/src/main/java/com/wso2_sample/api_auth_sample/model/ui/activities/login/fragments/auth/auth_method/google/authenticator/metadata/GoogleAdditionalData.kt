package com.wso2_sample.api_auth_sample.model.ui.activities.login.fragments.auth.auth_method.google.authenticator.metadata

data class GoogleAdditionalData (
    /**
     * Nonce
     */
    val nonce: String,
    /**
     * Client id
     */
    val clientId: String,
)