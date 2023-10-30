package com.example.api_auth_sample.model

/**
 * Authenticator data class
 */
data class Authenticator (
    val authenticator: String,
    val idp: String,
    val metadata: Any?
)

/**
 * IdpType enum class
 */
enum class IdpType(val idp: String) {
    GOOGLE("google"),
    LOCAL("LOCAL"),
    TOTP("totp")
}
