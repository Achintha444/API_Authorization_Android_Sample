package com.example.api_auth_sample.model

/**
 * AuthenticatorType enum class
 */
enum class AuthenticatorType(val authenticator: String) {
    /**
     * Google idp type
     */
    GOOGLE("Google"),
    /**
     * Local idp type
     */
    BASIC("Username & Password"),
    /**
     * TOTP idp type
     */
    TOTP("totp"),
    /**
     * FIDO idp type
     */
    FIDO("fido")
}
