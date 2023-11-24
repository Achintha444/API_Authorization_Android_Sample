package com.example.api_auth_sample.model.data.authenticator

/**
 * Authenticator data class
 */
data class Authenticator (
    /**
     * Authenticator id
     */
    val authenticatorId: String,
    /**
     * Authenticator type
     */
    val authenticator: String,
    /**
     * Idp type
     */
    val idp: String,
    /**
     * Authenticator metadata
     */
    val metadata: Any?,
    /**
     * Authenticator required parameters
     */
    val requiredParams: List<String>?
)
