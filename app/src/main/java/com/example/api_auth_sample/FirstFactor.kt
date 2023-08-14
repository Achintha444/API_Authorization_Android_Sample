package com.example.api_auth_sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import com.example.api_auth_sample.controller.AuthController
import com.example.api_auth_sample.databinding.ActivityFirstFactorBinding
import com.example.api_auth_sample.model.Authenticator
import com.example.api_auth_sample.util.Constants
import com.example.api_auth_sample.util.Util
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode

class FirstFactor : AppCompatActivity() {

    private lateinit var binding: ActivityFirstFactorBinding;
    private lateinit var authenticators: ArrayList<Authenticator>;
    private lateinit var orSignInText: LinearLayout;

    // authenticator views
    private lateinit var basicAuthView: FragmentContainerView;
    private lateinit var fidoAuthView: FragmentContainerView;
    private lateinit var googleIdpView: FragmentContainerView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        initializeComponents();
        setAuthenticators();

        // show authenticator based on the authenticators return
        AuthController.showAuthenticatorLayouts(
            authenticators,
            basicAuthView,
            fidoAuthView,
            googleIdpView
        );

        // hide `or sign in with` text
        hideOrSignInText();
    }

    private fun initializeComponents() {
        binding = ActivityFirstFactorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        orSignInText = findViewById(R.id.orSignInText);

        // set authenticator view
        basicAuthView = findViewById(R.id.basicAuthView);
        fidoAuthView = findViewById(R.id.fidoAuthView);
        googleIdpView = findViewById(R.id.googleIdpView);
    }

    private fun setAuthenticators() {
        val intent: Intent = intent;
        val authenticatorsString: String? = intent.getStringExtra("authenticators");
        val authenticatorNode: JsonNode = Util.getJsonObject(authenticatorsString!!);

        val authenticatorsTypeReference = object : TypeReference<ArrayList<Authenticator>>() {}

        authenticators = Util.jsonNodeToObject(authenticatorNode, authenticatorsTypeReference)
    }

    private fun hideOrSignInText() {
        if (AuthController.isAuthenticatorAvailable(authenticators, Constants.BASIC_AUTH) !== null
            && AuthController.numberOfAuthenticators(authenticators) > 1
        ) {

            orSignInText.visibility = View.GONE;
        }
    }
}
