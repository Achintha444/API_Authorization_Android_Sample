package com.example.api_auth_sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.api_auth_sample.controller.AuthController
import com.example.api_auth_sample.model.Authenticator
import com.example.api_auth_sample.util.Constants
import com.example.api_auth_sample.util.Util
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode

class AuthFragment : Fragment() {
    private lateinit var authenticators: ArrayList<Authenticator>
    private lateinit var orSignInText: LinearLayout

    // authenticator views
    private lateinit var basicAuthView: FragmentContainerView
    private lateinit var fidoAuthView: FragmentContainerView
    private lateinit var googleIdpView: FragmentContainerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_auth, container, false)
        initializeComponents(view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeComponents(view)

        var bundle: Bundle? = arguments;

        if (bundle != null) {
            setAuthenticators(bundle)
            // show authenticator based on the authenticators return
            AuthController.showAuthenticatorLayouts(
                authenticators,
                basicAuthView,
                fidoAuthView,
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
    }

    private fun setAuthenticators(bundle: Bundle) {

        val authenticatorsString: String? = bundle!!.getString("authenticatorsString")
        val authenticatorNode: JsonNode = Util.getJsonObject(authenticatorsString!!)
        val authenticatorsTypeReference = object : TypeReference<ArrayList<Authenticator>>() {}

        authenticators = Util.jsonNodeToObject(authenticatorNode, authenticatorsTypeReference)
    }

    private fun hideOrSignInText() {

        if (AuthController.isAuthenticatorAvailable(authenticators, Constants.BASIC_AUTH) != null
            && AuthController.numberOfAuthenticators(authenticators) > 1
        ) {
            orSignInText.visibility = View.VISIBLE
        }
    }
}