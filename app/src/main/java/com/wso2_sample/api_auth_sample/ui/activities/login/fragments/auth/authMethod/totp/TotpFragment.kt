package com.wso2_sample.api_auth_sample.ui.activities.login.fragments.auth.authMethod.totp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.fasterxml.jackson.databind.JsonNode
import com.wso2_sample.api_auth_sample.R
import com.wso2_sample.api_auth_sample.api.oauth_client.OauthClient
import com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.authMethods.AuthenticatorFragment
import com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.data.authenticator.Authenticator
import com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.AuthParams
import com.wso2_sample.api_auth_sample.model.data.authenticator.totp.TotpAuthParams
import com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.authMethods.totp.TotpContentListener

class TotpFragment : Fragment(), AuthenticatorFragment, TotpContentListener {

    private lateinit var totpButton: Button
    override var authenticator: Authenticator? = null
    private val totpContent = TotpContent()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View =
            inflater.inflate(R.layout.fragment_login_auth_auth_method_totp_totp, container, false)
        initializeComponents(view)

        setTotpContinueButtonListener()

        return view
    }

    private fun initializeComponents(view: View) {
        totpButton = view.findViewById(R.id.totpButton)
    }

    private fun setTotpContinueButtonListener() {
        totpButton.setOnClickListener {
            totpContent.setListener(this)
            totpContent.show(parentFragmentManager, TotpContent.TAG)
        }
    }

    override fun onTotpButtonClicked() {
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

    override fun getAuthParams(): AuthParams {
        val totp: String = totpContent.getTotpValue().text.toString()

        return TotpAuthParams(totp)
    }

    override fun onAuthorizeSuccess(authorizeObj: JsonNode) {
        // remove totp content bottom sheet
        totpContent.dismiss()

        this.handleActivityTransition(requireContext(), authorizeObj);

    }

    override fun onAuthorizeFail() {
        this.showSignInError(totpContent.getLayout(), requireContext())
    }

    override fun whenAuthorizing() {
        requireActivity().runOnUiThread {
            totpContent.getTotpContinueButton().isEnabled = false
        }
    }

    override fun finallyAuthorizing() {
        requireActivity().runOnUiThread {
            totpContent.getTotpContinueButton().isEnabled = true
        }
    }
}