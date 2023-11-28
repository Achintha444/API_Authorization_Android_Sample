package com.wso2_sample.api_auth_sample.ui.activities.login.fragments.auth.authMethod

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.wso2_sample.api_auth_sample.R
import com.wso2_sample.api_auth_sample.api.oauth_client.OauthClient
import com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.authMethods.AuthenticatorFragment
import com.wso2_sample.api_auth_sample.model.data.authenticator.Authenticator
import com.wso2_sample.api_auth_sample.model.ui.activities.login.fragments.auth.AuthParams
import com.fasterxml.jackson.databind.JsonNode

class BasicAuth : Fragment(), AuthenticatorFragment {

    private lateinit var signingBasicAuth: Button;
    private lateinit var username: EditText;
    private lateinit var password: EditText;
    private lateinit var layout: View;
    override var authenticator: Authenticator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View =
            inflater.inflate(R.layout.fragment_login_auth_auth_method_basic_auth, container, false)
        initializeComponents(view)

        // set on-click listener
        signingBasicAuth.setOnClickListener {
            OauthClient.authenticate(
                requireContext(),
                authenticator!!,
                getAuthParams(),
                ::whenAuthorizing,
                ::finallyAuthorizing,
                ::onAuthorizeSuccess,
                ::onAuthorizeFail
            )
        }

        return view
    }

    private fun initializeComponents(view: View) {
        signingBasicAuth = view.findViewById(R.id.signinBasicAuth)
        username = view.findViewById(R.id.username)
        password = view.findViewById(R.id.password)
        layout = view.findViewById(R.id.basicAuthlayout)
    }

    override fun getAuthParams(): AuthParams {
        val usernameText: String = username.text.toString().ifEmpty {
            getString(R.string.activity_login_auth_auth_method_basic_auth_username)
        }
        val passwordText: String = password.text.toString().ifEmpty {
            getString(R.string.activity_login_auth_auth_method_basic_auth_password)
        }

        return AuthParams(username = usernameText, password = passwordText)
    }

    override fun onAuthorizeSuccess(authorizeObj: JsonNode) {
        this.handleActivityTransition(requireContext(), authorizeObj);
    }

    override fun onAuthorizeFail() {
        this.showSignInError(layout, requireContext())
    }

    override fun whenAuthorizing() {
        requireActivity().runOnUiThread {
            signingBasicAuth.isEnabled = false;
        }
    }

    override fun finallyAuthorizing() {
        requireActivity().runOnUiThread {
            signingBasicAuth.isEnabled = true;
        }
    }
}