package com.example.api_auth_sample.ui.auth.authMethod

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.api_auth_sample.R
import com.example.api_auth_sample.api.APICall
import com.example.api_auth_sample.model.AuthParams
import com.example.api_auth_sample.model.Authenticator
import com.example.api_auth_sample.model.AuthenticatorFragment
import com.example.api_auth_sample.ui.Factor
import com.example.api_auth_sample.ui.SignedInInterface
import com.example.api_auth_sample.util.UiUtil
import com.fasterxml.jackson.databind.JsonNode

class GoogleFragment : Fragment(), AuthenticatorFragment {

    private lateinit var googleButton: Button
    private lateinit var layout: View
    override var authenticator: Authenticator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_google, container, false)

        initializeComponents(view)

        googleButton.setOnClickListener {
            APICall.authenticate(
                authenticator!!,
                getAuthParams(),
                ::whenAuthorizing,
                ::finallyAuthorizing,
                ::onAuthorizeSuccess,
                ::onAuthorizeFail
            );
        }

        return view
    }

    private fun initializeComponents(view: View) {
        googleButton = view.findViewById(R.id.googleButton)
        layout = view.findViewById(R.id.googleIdpView)
    }

    override fun getAuthParams(): AuthParams {
        return AuthParams(code = "code", state = "state")
    }

    override fun onAuthorizeSuccess(authorizeObj: JsonNode) {
        if(authorizeObj["currentStep"] != null) {
            val intent = Intent(requireActivity(), Factor::class.java);
            intent.putExtra(
                "authenticators",
                authorizeObj["currentStep"]["authenticators"].toString()
            );
            startActivity(intent)
        } else {
            val intent = Intent(requireActivity(), SignedInInterface::class.java);
            startActivity(intent)
        }
    }

    override fun onAuthorizeFail() {
        UiUtil.showSnackBar(layout, "Sign in Failure");
    }

    override fun whenAuthorizing() {
        requireActivity().runOnUiThread {
            googleButton.isEnabled = false;
        }
    }

    override fun finallyAuthorizing() {
        requireActivity().runOnUiThread {
            googleButton.isEnabled = true;
        }
    }
}