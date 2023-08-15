package com.example.api_auth_sample.controller

import android.view.View
import com.example.api_auth_sample.model.Authenticator
import com.example.api_auth_sample.util.Constants
import com.example.api_auth_sample.util.Util
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class AuthController {
    companion object {
        fun isAuthenticatorAvailable(
            authenticators: ArrayList<Authenticator>,
            authenticatorType: String
        ): Authenticator? {
            return authenticators.find {
                it.authenticator == authenticatorType
            }
        }

        fun numberOfAuthenticators(
            authenticators: ArrayList<Authenticator>,
        ): Int {
            return authenticators.size;
        }

        fun showAuthenticatorLayouts(
            authenticators: ArrayList<Authenticator>, basicAuthView: View?, fidoAuthView: View?,
            googleIdpView: View?
        ) {
            authenticators.forEach {
                showAuthenticator(it, basicAuthView, fidoAuthView, googleIdpView);
            }
        }

        private fun showAuthenticator(
            authenticator: Authenticator, basicAuthView: View?, fidoAuthView: View?,
            googleIdpView: View?
        ) {
            when (authenticator.authenticator) {
                Constants.BASIC_AUTH -> basicAuthView!!.visibility = View.VISIBLE;

                Constants.FIDO -> fidoAuthView!!.visibility = View.VISIBLE;

                Constants.OPENID -> showIdps(authenticator.idp, googleIdpView)
            }
        }

        private fun showIdps(idpType: String, googleIdpView: View?) {
            when (idpType) {
                Constants.GOOGLE_IDP -> googleIdpView!!.visibility = View.VISIBLE;
            }
        }

        // get param body for basic auth
        private fun getparamBodyForBasicAuth(username: String, password: String): LinkedHashMap<String, String> {
            val paramBody = LinkedHashMap<String, String>();
            paramBody["username"] = "username";
            paramBody["password"] = "password";

            return paramBody;
        }

        // get param body for google idp
        private fun getparamBodyForGoogle (code: String, state: String): LinkedHashMap<String, String> {
            val paramBody = LinkedHashMap<String, String>();
            paramBody["authenticator"] = Constants.GOOGLE_OPENID
            paramBody["idp"] = Constants.GOOGLE_IDP
            paramBody["code"] = "username";
            paramBody["state"] = "password";

            return paramBody;
        }

        // get param body for fido
        private fun getparamBodyForFido(tokenResponse: String): LinkedHashMap<String, String> {
            val paramBody = LinkedHashMap<String, String>();
            paramBody["authenticator"] = Constants.FIDO
            paramBody["idp"] = Constants.LOCAL_IDP
            paramBody["tokenResponse"] = tokenResponse

            return paramBody;
        }

        // get param body for fido
        private fun getparamBodyForTotp(otp: String): LinkedHashMap<String, String> {
            val paramBody = LinkedHashMap<String, String>();
            paramBody["authenticator"] = Constants.TOTP_IDP
            paramBody["idp"] = Constants.LOCAL_IDP
            paramBody["otp"] = otp

            return paramBody;
        }

        fun buildRequestBodyForAuth(authenticator: Authenticator): RequestBody {

            val authBody = LinkedHashMap<String, Any>();
            authBody["flowId"] = "3bd1f207-e5b5-4b45-8a91-13b0acfb2151";
            authBody["nonce"] = "e24edfeas1";

            when(authenticator.authenticator) {
                Constants.BASIC_AUTH -> {
                    authBody["authenticator"] = authenticator;
                    authBody["idp"] = Constants.LOCAL_IDP;
                    authBody["params"] = getparamBodyForBasicAuth("username", "password")
                }

                Constants.GOOGLE_OPENID -> authBody["params"] = getparamBodyForGoogle("cod", "state")

                Constants.TOTP_IDP -> authBody["params"] = getparamBodyForTotp("1234")

                Constants.FIDO -> authBody["params"] = getparamBodyForFido("token")
            }

            val authObject: String = Util.getJsonObject(authBody).toString();

            authObject.toRequestBody("application/json".toMediaTypeOrNull())

            return authObject.toRequestBody("application/json".toMediaTypeOrNull())
        }
    }
}