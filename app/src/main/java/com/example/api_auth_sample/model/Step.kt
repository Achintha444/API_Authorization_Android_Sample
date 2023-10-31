package com.example.api_auth_sample.model

/**
 * Authenticator data class
 */
data class Step (
    /**
     * Authenticator id
     */
    val stepType: String,
    /**
     * Authenticator type
     */
    val authenticators: ArrayList<Authenticator>
)
