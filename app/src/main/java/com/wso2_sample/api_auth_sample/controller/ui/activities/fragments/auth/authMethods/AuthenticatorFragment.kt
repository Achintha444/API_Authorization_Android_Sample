package com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.authMethods

import android.content.Context
import android.content.Intent
import android.view.View
import com.wso2_sample.api_auth_sample.R
import com.wso2_sample.api_auth_sample.model.ui.activities.login.fragments.auth.AuthParams
import com.wso2_sample.api_auth_sample.ui.activities.home.Home
import com.wso2_sample.api_auth_sample.ui.activities.login.Login
import com.wso2_sample.api_auth_sample.util.UiUtil
import com.fasterxml.jackson.databind.JsonNode
import com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.data.authenticator.Authenticator

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
        if (authorizeObj["nextStep"] != null) {
            val intent = Intent(context, Login::class.java);
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
            val intent = Intent(context, Home::class.java);
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
        UiUtil.showSnackBar(layout, context.getString(R.string.error_sign_in_failed))
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

