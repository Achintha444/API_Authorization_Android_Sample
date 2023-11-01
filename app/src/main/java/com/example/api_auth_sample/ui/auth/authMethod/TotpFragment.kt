package com.example.api_auth_sample.ui.auth.authMethod

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.api_auth_sample.R
import com.example.api_auth_sample.api.APICall
import com.example.api_auth_sample.model.AuthParams
import com.example.api_auth_sample.model.Authenticator
import com.example.api_auth_sample.model.AuthenticatorFragment
import com.fasterxml.jackson.databind.JsonNode

class TotpFragment : Fragment(), AuthenticatorFragment {

    private lateinit var layout: View
    private lateinit var totpContinueButton: Button
    private lateinit var totpValue: EditText
    override var authenticator: Authenticator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_totp, container, false)
        initializeComponents(view)

        setTotpContinueButtonListener()

        return view
    }

    private fun initializeComponents(view: View) {
        layout = view.findViewById(R.id.totpIdpView)
        totpContinueButton = view.findViewById(R.id.totpContinueButton)
        totpValue = view.findViewById(R.id.totpValue)
    }

    private fun setTotpContinueButtonListener() {
        totpContinueButton.setOnClickListener {
            APICall.authenticate(
                requireContext(),
                authenticator!!,
                getAuthParams(),
                ::whenAuthorizing,
                ::finallyAuthorizing,
                ::onAuthorizeSuccess,
                ::onAuthorizeFail
            )
        }
    }

    override fun getAuthParams(): AuthParams {
        val totp: String = totpValue.text.toString()

        return AuthParams(totp = totp)
    }

    override fun onAuthorizeSuccess(authorizeObj: JsonNode) {
        this.handleActivityTransition(requireContext(), authorizeObj);
    }

    override fun onAuthorizeFail() {
        this.showSignInError(layout)
    }

    override fun whenAuthorizing() {
        requireActivity().runOnUiThread {
            totpContinueButton.isEnabled = false
        }
    }

    override fun finallyAuthorizing() {
        requireActivity().runOnUiThread {
            totpContinueButton.isEnabled = true
        }
    }
}