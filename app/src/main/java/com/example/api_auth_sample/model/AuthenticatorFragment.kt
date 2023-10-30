package com.example.api_auth_sample.model

import com.fasterxml.jackson.databind.JsonNode

interface AuthenticatorFragment {

    var authenticator: Authenticator?;

    fun updateAuthenticator(authenticator: Authenticator) {
        this.authenticator = authenticator;
    }
    fun getAuthParams(): AuthParams

    fun onAuthorizeSuccess(authorizeObj: JsonNode)

    fun onAuthorizeFail()

    fun whenAuthorizing()

    fun finallyAuthorizing()
}
