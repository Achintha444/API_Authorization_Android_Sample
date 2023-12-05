package com.wso2_sample.api_auth_sample.ui.activities.login.fragments.auth.auth_method

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPublicKeyCredentialOption
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.wso2_sample.api_auth_sample.R
import com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.AuthController
import com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.AuthParams
import com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.auth_method.AuthenticatorFragment
import com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.data.authenticator.Authenticator
import com.wso2_sample.api_auth_sample.model.api.oauth_client.AuthorizeFlow
import com.wso2_sample.api_auth_sample.model.ui.activities.login.fragments.auth.auth_method.passkey.authenticator.PasskeyAuthParams
import com.wso2_sample.api_auth_sample.model.ui.activities.login.fragments.auth.auth_method.passkey.authenticator.PasskeyAuthenticator
import com.wso2_sample.api_auth_sample.model.ui.activities.login.fragments.auth.auth_method.passkey.authenticator.passkey_data.PasskeyChallenge
import kotlinx.coroutines.launch

class PasskeyFragment : Fragment(), AuthenticatorFragment {

    private lateinit var passkeyButton: Button
    private lateinit var layout: View
    override var authenticator: Authenticator? = null

    private lateinit var publicKeyCredentialOption: GetPublicKeyCredentialOption
    private lateinit var requestJson: PasskeyChallenge
    private var result: GetCredentialResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View =
            inflater.inflate(R.layout.fragment_login_auth_auth_method_passkey, container, false)

        initializeComponents(view)
        setPasskeyButtonClickListener()

        return view;
    }

    override fun updateAuthenticator(authenticator: Authenticator) {
        this.authenticator = authenticator

        setRequestJson()
        setPublicKeyCredentialOption()
    }

    private fun setPasskeyButtonClickListener() {
        if (::passkeyButton.isInitialized) {
            passkeyButton.setOnClickListener {
                lifecycleScope.launch {
                    setResult()

                    // Ensure that result is not null before using it
                    result?.let { result ->
                        AuthController.handleSignIn(result)
                    } ?: run {
                        view?.let {
                            onAuthorizeFail()
                        }
                    }

//                    OauthClient.authenticate(
//                        requireContext(),
//                        authenticator!!,
//                        getAuthParams(),
//                        ::whenAuthorizing,
//                        ::finallyAuthorizing,
//                        ::onAuthorizeSuccess,
//                        ::onAuthorizeFail
//                    )
                }
            }
        }
    }

    private fun initializeComponents(view: View) {
        passkeyButton = view.findViewById(R.id.passkeyButton)
        layout = view.findViewById(R.id.passkeyLayout)
    }

    private fun setRequestJson() {
        val challengeString: String =
            (authenticator as PasskeyAuthenticator).metadata.additionalData.challengeData

        requestJson = PasskeyChallenge.getPasskeyChallengeFromChallengeString(challengeString)
    }

    private fun setPublicKeyCredentialOption() {
        publicKeyCredentialOption = GetPublicKeyCredentialOption(

            requestJson = requestJson.toString()
        )
    }

    private suspend fun setResult() {
        val credentialManager: CredentialManager = CredentialManager.create(requireContext())

        try {
            val getCredRequest = GetCredentialRequest(
                listOf(publicKeyCredentialOption)
            )

            result = credentialManager.getCredential(
                // Use an activity-based context to avoid undefined system UI
                // launching behavior.
                context = requireContext(),
                request = getCredRequest,

                )
        } catch (e: NoCredentialException) {
            Log.e("CredentialManager", "No credential available", e)
        } catch (e: GetCredentialCancellationException) {
            Log.e("CredentialManager", "GetCredentialCancellationException", e)
        }
    }

    override fun getAuthParams(): AuthParams {
        return PasskeyAuthParams("tokenResponse")
    }

    override fun onAuthorizeSuccess(authorizeFlow: AuthorizeFlow?) {
        this.handleActivityTransition(requireContext(), authorizeFlow);
    }

    override fun onAuthorizeFail() {
        this.showSignInError(layout, requireContext())
    }

    override fun whenAuthorizing() {
        requireActivity().runOnUiThread {
            passkeyButton.isEnabled = false;
        }
    }

    override fun finallyAuthorizing() {
        requireActivity().runOnUiThread {
            passkeyButton.isEnabled = true;
        }
    }

}