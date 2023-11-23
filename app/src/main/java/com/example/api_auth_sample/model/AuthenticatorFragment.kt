package com.example.api_auth_sample.model

import android.content.Context
import android.content.Intent
import android.view.View
import com.example.api_auth_sample.R
import com.example.api_auth_sample.ui.Factor
import com.example.api_auth_sample.ui.SignedInInterface
import com.example.api_auth_sample.util.UiUtil
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
     * Get authenticator params
     */
    fun getAuthParams(): AuthParams

    /**
     * On authorize success
     */
    fun onAuthorizeSuccess(authorizeObj: JsonNode)

    /**
     * Handle activity transition
     */
    fun handleActivityTransition(context: Context, authorizeObj: JsonNode) {
        if(authorizeObj["nextStep"] != null) {
            val intent = Intent(context, Factor::class.java);
            intent.putExtra(
                "flowId",
                authorizeObj["flowId"].toString()
            );
            intent.putExtra(
                "step",
                authorizeObj["nextStep"].toString()
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
     * Show sign in error
     */
    fun showSignInError(layout: View, context: Context) {
        UiUtil.showSnackBar(layout, context.getString(R.string.sign_in_failed))
    }

    /**
     * When authorizing
     */
    fun whenAuthorizing()

    /**
     * After authorizing
     */
    fun finallyAuthorizing()
}
