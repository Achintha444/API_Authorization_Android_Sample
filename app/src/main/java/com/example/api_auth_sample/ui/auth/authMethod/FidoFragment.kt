package com.example.api_auth_sample.ui.auth.authMethod

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.api_auth_sample.R
import com.example.api_auth_sample.api.OauthClient
import com.example.api_auth_sample.model.AuthParams
import com.example.api_auth_sample.model.Authenticator
import com.example.api_auth_sample.model.AuthenticatorFragment
import com.fasterxml.jackson.databind.JsonNode

class FidoFragment : Fragment(), AuthenticatorFragment {

    private lateinit var fidoButton: Button
    private lateinit var layout: View
    override var authenticator: Authenticator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_fido, container, false)

        initializeComponents(view)

        fidoButton.setOnClickListener {
            OauthClient.authenticate(
                requireContext(),
                authenticator!!,
                getAuthParams(),
                ::whenAuthorizing,
                ::finallyAuthorizing,
                ::onAuthorizeSuccess,
                ::onAuthorizeFail
            );
        }

        return view;
    }

    private fun initializeComponents(view: View) {
        fidoButton = view.findViewById(R.id.fidoButton)
        layout = view.findViewById(R.id.fidoLayout)
    }

    override fun getAuthParams(): AuthParams {
       return AuthParams(tokenResponse = "tokenResponse")
    }

    override fun onAuthorizeSuccess(authorizeObj: JsonNode) {
        this.handleActivityTransition(requireContext(), authorizeObj);
    }

    override fun onAuthorizeFail() {
        this.showSignInError(layout, requireContext())
    }

    override fun whenAuthorizing() {
        requireActivity().runOnUiThread {
            fidoButton.isEnabled = false;
        }
    }

    override fun finallyAuthorizing() {
        requireActivity().runOnUiThread {
            fidoButton.isEnabled = true;
        }
    }

}