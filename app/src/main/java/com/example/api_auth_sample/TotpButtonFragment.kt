package com.example.api_auth_sample

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.api_auth_sample.api.APICall
import com.example.api_auth_sample.model.AuthParams
import com.example.api_auth_sample.model.Authenticator
import com.example.api_auth_sample.model.AuthenticatorFragment
import com.example.api_auth_sample.util.UiUtil
import com.fasterxml.jackson.databind.JsonNode

class TotpButtonFragment : Fragment(), AuthenticatorFragment {

    private lateinit var totpButton: Button
    private lateinit var layout: View
    override var authenticator: Authenticator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_totp_button, container, false)

        initializeComponents(view)

        totpButton.setOnClickListener {
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
        totpButton = view.findViewById(R.id.totpButton)
        layout = view.findViewById(R.id.totpIdpView)
    }

    override fun getAuthParams(): AuthParams {
        return AuthParams(otp = "1234")
    }

    override fun onAuthorizeSuccess(authorizeObj: JsonNode) {
        if (authorizeObj["currentStep"] != null) {
            val intent = Intent(requireActivity(), FirstFactor::class.java)
            intent.putExtra(
                "authenticators",
                authorizeObj["currentStep"]["authenticators"].toString()
            )
            startActivity(intent)
        } else {
            val intent = Intent(requireActivity(), SignedInInterface::class.java)
            startActivity(intent)
        }
    }

    override fun onAuthorizeFail() {
        UiUtil.showSnackBar(layout, "Sign in Failure")
    }

    override fun whenAuthorizing() {
        requireActivity().runOnUiThread {
            totpButton.isEnabled = false
        }
    }

    override fun finallyAuthorizing() {
        requireActivity().runOnUiThread {
            totpButton.isEnabled = true
        }
    }
}
