package com.example.api_auth_sample.model

import android.content.Intent
import com.example.api_auth_sample.FirstFactor
import com.example.api_auth_sample.SignedInInterface
import com.example.api_auth_sample.util.UiUtil
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
