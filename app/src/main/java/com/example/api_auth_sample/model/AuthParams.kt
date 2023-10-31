package com.example.api_auth_sample.model

/**
 * Authenticator parameters data class
 */
data class AuthParams (
    /**
     * Username
     */
    val username: String? = null,
    /**
     * Password
     */
    val password: String? = null,
    /**
     * Code
     */
    val code: String? = null,
    /**
     * State
     */
    val state: String? = null,
    /**
     * Otp code
     */
    val otp: String? = null,
    /**
     * Token response
     */
    val tokenResponse: String? = null
)
