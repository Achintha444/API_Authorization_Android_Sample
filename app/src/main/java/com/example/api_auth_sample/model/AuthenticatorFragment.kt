package com.example.api_auth_sample.model

interface AuthenticatorFragment {

    var authenticator: Authenticator?;

    fun updateAuthenticator(authenticator: Authenticator) {
        this.authenticator = authenticator;
    }

    fun getAuthParams(): AuthParams
}
