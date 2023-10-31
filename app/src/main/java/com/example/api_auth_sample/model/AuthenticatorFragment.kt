package com.example.api_auth_sample.model

import android.content.Context
import android.content.Intent
import com.example.api_auth_sample.ui.Factor
import com.example.api_auth_sample.ui.SignedInInterface
import com.fasterxml.jackson.databind.JsonNode

/**
 * Authenticator fragment interface
 */
interface AuthenticatorFragment {

    /**
     * Authenticator of the fragment
     */
    var authenticator: Authenticator?;

    /**
     * Update authenticator
     */
    fun updateAuthenticator(authenticator: Authenticator) {
        this.authenticator = authenticator;
    }

    /**
     * Get autheticator params
     */
    fun getAuthParams(): AuthParams

    /**
     * On authorize success
     */
    fun onAuthorizeSuccess(context: Context, authorizeObj: JsonNode) {
        if(authorizeObj["nextStep"] != null) {
            val intent = Intent(context, Factor::class.java);
            intent.putExtra(
                "authenticators",
                authorizeObj["nextStep"]["authenticators"].toString()
            );
            context.startActivity(intent)
        } else {
            val intent = Intent(context, SignedInInterface::class.java);
            context.startActivity(intent)
        }
    }

    /**
     * On authorize fail
     */
    fun onAuthorizeFail()

    /**
     * When authorizing
     */
    fun whenAuthorizing()

    /**
     * After authorizing
     */
    fun finallyAuthorizing()
}
