package com.wso2_sample.api_auth_sample.model.data.authenticator

/**
 * AuthenticatorType enum class
 */
enum class AuthenticatorType(val authenticator: String) {
    /**
     * Google authenticator type
     */
    GOOGLE("Google"),

    /**
     * Local authenticator type
     */
    BASIC("Username & Password"),

    /**
     * TOTP authenticator type
     */
    TOTP("TOTP"),

    /**
     * FIDO authenticator type
     */
    PASSKEY("Passkey")
}
