package com.wso2_sample.api_auth_sample.ui.activities.login.fragments.auth.authMethod

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.wso2_sample.api_auth_sample.R
import com.wso2_sample.api_auth_sample.api.oauth_client.OauthClient
import com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.authMethods.AuthenticatorFragment
import com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.AuthParams
import com.wso2_sample.api_auth_sample.model.ui.activities.login.fragments.auth.authMethod.google.GoogleSignInActivityResultContract
import com.wso2_sample.api_auth_sample.util.config.OauthClientConfiguration
import com.fasterxml.jackson.databind.JsonNode
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.data.authenticator.Authenticator
import com.wso2_sample.api_auth_sample.model.data.authenticator.google.GoogleAuthParams

class GoogleFragment : Fragment(), AuthenticatorFragment {

    private val tag = "GoogleFragment"

    private lateinit var googleButton: Button
    private lateinit var layout: View
    override var authenticator: Authenticator? = null

    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var signInLauncher: ActivityResultLauncher<Unit>
    private lateinit var googleAccount: GoogleSignInAccount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(OauthClientConfiguration.getInstance(requireContext()).googleWebClientId)
            .requestIdToken(OauthClientConfiguration.getInstance(requireContext()).googleWebClientId)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)

        signInLauncher = registerForActivityResult(
            GoogleSignInActivityResultContract(mGoogleSignInClient)
        ) { result ->
            if (result != null) {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(result)
                handleSignInResult(task)
            } else {
                // Handle the sign-in failure or cancellation
                onAuthorizeFail()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View =
            inflater.inflate(R.layout.fragment_login_auth_auth_method_google, container, false)

        initializeComponents(view)

        googleButtonOnClick()

        return view
    }

    private fun initializeComponents(view: View) {
        googleButton = view.findViewById(R.id.googleButton)

        layout = view.findViewById(R.id.googleIdpView)
    }

    override fun getAuthParams(): AuthParams {
        return GoogleAuthParams(
            googleAccount.serverAuthCode,
            googleAccount.idToken
        )
    }

    override fun onAuthorizeSuccess(authorizeObj: JsonNode) {
        this.handleActivityTransition(requireContext(), authorizeObj)
    }

    override fun onAuthorizeFail() {
        this.showSignInError(layout, requireContext())
    }

    override fun whenAuthorizing() {
        requireActivity().runOnUiThread {
            googleButton.isEnabled = false
        }
    }

    override fun finallyAuthorizing() {
        requireActivity().runOnUiThread {
            googleButton.isEnabled = true
        }
    }

    private fun googleButtonOnClick() {
        googleButton.setOnClickListener {
            signInLauncher.launch(Unit)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            googleAccount = completedTask.getResult(ApiException::class.java)

            OauthClient.authenticate(
                requireContext(),
                authenticator!!,
                getAuthParams(),
                ::whenAuthorizing,
                ::finallyAuthorizing,
                ::onAuthorizeSuccess,
                ::onAuthorizeFail
            )
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(tag, "signInResult:failed code=" + e.statusCode)
            onAuthorizeFail()
        }
    }
}