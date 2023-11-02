package com.example.api_auth_sample.controller

import android.view.View
import com.example.api_auth_sample.model.AuthParams
import com.example.api_auth_sample.model.Authenticator
import com.example.api_auth_sample.model.AuthenticatorType
import com.example.api_auth_sample.util.Constants
import com.example.api_auth_sample.util.Util
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * AuthController class
 */
class AuthController {
    companion object {

        /**
         * Check if authenticator is available in the authenticators list
         */
        fun isAuthenticatorAvailable(
            authenticators: ArrayList<Authenticator>,
            authenticatorType: AuthenticatorType
        ): Authenticator? {
            return authenticators.find {
                it.authenticator == authenticatorType.authenticator
            }
        }

        /**
         * Get number of authenticators in the authenticators list
         */
        fun numberOfAuthenticators(
            authenticators: ArrayList<Authenticator>,
        ): Int {
            return authenticators.size;
        }

        /**
         * Show authenticator layouts
         */
        fun showAuthenticatorLayouts(
            authenticators: ArrayList<Authenticator>, basicAuthView: View?, fidoAuthView: View?,
            totpAuthView: View?, googleIdpView: View?
        ) {
            authenticators.forEach {
                showAuthenticator(it, basicAuthView, fidoAuthView, totpAuthView, googleIdpView);
            }
        }

        /**
         * Show authenticator layouts
         */
        private fun showAuthenticator(
            authenticator: Authenticator, basicAuthView: View?, fidoAuthView: View?,
            totpAuthView: View?, googleIdpView: View?
        ) {
            when (authenticator.authenticator) {
                AuthenticatorType.BASIC.authenticator -> basicAuthView!!.visibility = View.VISIBLE;

                AuthenticatorType.PASSKEY.authenticator -> fidoAuthView!!.visibility = View.VISIBLE;

                AuthenticatorType.TOTP.authenticator-> totpAuthView!!.visibility = View.VISIBLE;

                AuthenticatorType.GOOGLE.authenticator -> googleIdpView!!.visibility = View.VISIBLE
            }
        }

        private fun showIdps(idpType: String, googleIdpView: View?) {
            when (idpType) {
                Constants.GOOGLE_IDP -> googleIdpView!!.visibility = View.VISIBLE;
            }
        }

        /**
         * Get param body for basic auth
         */
        private fun getParamBodyForBasicAuth(
            username: String,
            password: String
        ): LinkedHashMap<String, String> {
            val paramBody = LinkedHashMap<String, String>();
            paramBody["username"] = username;
            paramBody["password"] = password;

            return paramBody;
        }

        /**
         * Get param body for google
         */
        private fun getParamBodyForGoogle(
            accessToken: String,
            idToken: String
        ): LinkedHashMap<String, String> {
            val paramBody = LinkedHashMap<String, String>();
            paramBody["idToken"] = idToken;
            paramBody["accessToken"] = accessToken;

            return paramBody;
        }

        /**
         * Get param body for fido
         */
        private fun getParamBodyForFido(tokenResponse: String): LinkedHashMap<String, String> {
            val paramBody = LinkedHashMap<String, String>();
            paramBody["authenticator"] = Constants.FIDO
            paramBody["idp"] = Constants.LOCAL_IDP
            paramBody["tokenResponse"] = tokenResponse

            return paramBody;
        }

        /**
         * Get param body for totp
         */
        private fun getParamBodyForTotp(otp: String): LinkedHashMap<String, String> {
            val paramBody = LinkedHashMap<String, String>();
            paramBody["token"] = otp

            return paramBody;
        }

        /**
         * Build request body for auth
         */
        fun buildRequestBodyForAuth(
            flowId: String?,
            authenticator: Authenticator,
            authParams: AuthParams
        ): RequestBody {

            val authBody = LinkedHashMap<String, Any>();
            authBody["flowId"] = flowId!!;

            val selectedAuthenticator = LinkedHashMap<String, Any>();
            selectedAuthenticator["authenticatorId"] = authenticator.authenticatorId;
            when (authenticator.authenticator) {
                AuthenticatorType.BASIC.authenticator -> {
                    selectedAuthenticator["params"] =
                        getParamBodyForBasicAuth(authParams.username!!, authParams.password!!)
                }

                AuthenticatorType.GOOGLE.authenticator -> selectedAuthenticator["params"] =
                    getParamBodyForGoogle(authParams.accessToken!!, authParams.idToken!!)

                AuthenticatorType.TOTP.authenticator -> selectedAuthenticator["params"] =
                    getParamBodyForTotp(authParams.totp!!)

                AuthenticatorType.PASSKEY.authenticator -> selectedAuthenticator["params"] =
                    getParamBodyForFido(authParams.tokenResponse!!)
            }

            authBody["selectedAuthenticator"] = selectedAuthenticator;

            return Util.getJsonObject(authBody).toString()
                .toRequestBody("application/json".toMediaTypeOrNull())
        }
    }
}