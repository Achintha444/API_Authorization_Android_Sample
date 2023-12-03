package com.wso2_sample.api_auth_sample.model.data.authenticator.google.metadata

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