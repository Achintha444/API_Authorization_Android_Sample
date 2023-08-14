package com.example.api_auth_sample.controller

import android.view.View
import com.example.api_auth_sample.model.Authenticator
import com.example.api_auth_sample.util.Constants

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
    }
}