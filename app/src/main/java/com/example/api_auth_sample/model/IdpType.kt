package com.example.api_auth_sample.model

/**
 * IdpType enum class
 */
enum class IdpType(val idp: String) {
    /**
     * Google idp type
     */
    GOOGLE("Google"),
    /**
     * Local idp type
     */
    LOCAL("LOCAL"),
    /**
     * TOTP idp type
     */
    TOTP("totp"),
    /**
     * FIDO idp type
     */
    FIDO("fido")
}
