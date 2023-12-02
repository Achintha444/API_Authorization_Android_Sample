package com.wso2_sample.api_auth_sample.ui.activities.login.fragments.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.wso2_sample.api_auth_sample.R
import com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.AuthController
import com.wso2_sample.api_auth_sample.model.data.authenticator.Authenticator
import com.wso2_sample.api_auth_sample.model.data.authenticator.AuthenticatorType
import com.wso2_sample.api_auth_sample.model.ui.activities.login.fragments.auth.Step
import com.wso2_sample.api_auth_sample.model.util.uiUtil.SharedPreferencesKeys
import com.wso2_sample.api_auth_sample.util.UiUtil
import com.wso2_sample.api_auth_sample.util.Util
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode

class AuthFragment : Fragment() {
    private lateinit var flowId: String

    private lateinit var authenticators: ArrayList<Authenticator>
    private lateinit var orSignInText: LinearLayout

    // authenticator views
    private lateinit var basicAuthView: FragmentContainerView
    private lateinit var fidoAuthView: FragmentContainerView
    private lateinit var googleIdpView: FragmentContainerView
    private lateinit var totpIdpView: FragmentContainerView

    private lateinit var authListener: AuthListener;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login_auth, container, false)
        initializeComponents(view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = arguments;

        if (bundle != null) {
            setAuthenticators(bundle)
            setFlowId(bundle)

            passAuthenticatorToAuthFragment()

            // show authenticator based on the authenticators return
            AuthController.showAuthenticatorLayouts(
                authenticators,
                basicAuthView,
                fidoAuthView,
                totpIdpView,
                googleIdpView
            )

            // hide `or sign in with` text
            hideOrSignInText()
        }
    }

    private fun initializeComponents(view: View) {
        orSignInText = view.findViewById(R.id.orSignInText)

        // set authenticator view
        basicAuthView = view.findViewById(R.id.basicAuthView)
        fidoAuthView = view.findViewById(R.id.fidoAuthView)
        googleIdpView = view.findViewById(R.id.googleIdpView)
        totpIdpView = view.findViewById(R.id.totpIdpView)
    }

    private fun setAuthenticators(bundle: Bundle) {

        val stepString: String? = bundle.getString("stepString")
        val stepNode: JsonNode = Util.getJsonObject(stepString!!)
        val stepTypeReference = object : TypeReference<Step>() {}

        authenticators = Util.jsonNodeToObject(stepNode, stepTypeReference).authenticators
    }

    private fun setFlowId(bundle: Bundle) {
        val flowIdString: String? = bundle.getString("flowId")
        val flowIdJson: JsonNode = Util.getJsonObject(flowIdString!!)
        val flowIdTypeReference = object : TypeReference<String>() {}
        flowId = Util.jsonNodeToObject(flowIdJson, flowIdTypeReference)

        // save flowId to shared preferences to be used when authenticating user
        UiUtil.writeToSharedPreferences(
            requireContext().getSharedPreferences(
                R.string.app_name.toString(),
                Context.MODE_PRIVATE
            ), SharedPreferencesKeys.FLOW_ID.key, flowId
        )
    }

    private fun hideOrSignInText() {

        if (AuthController.isAuthenticatorAvailable(authenticators, AuthenticatorType.BASIC) != null
            && AuthController.numberOfAuthenticators(authenticators) > 1
        ) {
            orSignInText.visibility = View.VISIBLE
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AuthListener) {
            authListener = context
        }
    }

    private fun passAuthenticatorToAuthFragment() {

        authenticators.forEach {
            authListener.onAuthenticatorPassed(it)
        }
    }

    interface AuthListener {
        fun onAuthenticatorPassed(authenticator: Authenticator)
    }
}