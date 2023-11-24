package com.example.api_auth_sample.model.ui.activities.login.fragments.auth

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
    val accessToken: String? = null,
    /**
     * State
     */
    val idToken: String? = null,
    /**
     * Otp code
     */
    val totp: String? = null,
    /**
     * Token response
     */
    val tokenResponse: String? = null
)
