package com.example.api_auth_sample.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.api_auth_sample.R
import com.example.api_auth_sample.databinding.ActivityFactorBinding
import com.example.api_auth_sample.model.Authenticator
import com.example.api_auth_sample.model.AuthenticatorFragment
import com.example.api_auth_sample.ui.auth.AuthFragment
import com.example.api_auth_sample.util.Constants

class Factor : AppCompatActivity(), AuthFragment.AuthListener {

    private lateinit var binding: ActivityFactorBinding
    private lateinit var authenticatorsString: String
    private lateinit var fragmentManager: FragmentManager
    private lateinit var authFragment: AuthFragment
    private lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setAuthenticatorsString()

        initalizeAuthFragment()

        initializeComponents()
    }

    private fun initializeComponents() {
        binding = ActivityFactorBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setAuthenticatorsString() {
        val intent: Intent = intent
        authenticatorsString = intent.getStringExtra("authenticators")!!
    }

    private fun initalizeAuthFragment() {
        fragmentManager = supportFragmentManager
        val mFragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        authFragment = AuthFragment()

        bundle = Bundle()
        bundle.putString("authenticatorsString", authenticatorsString)
        authFragment.arguments = bundle
        mFragmentTransaction.add(R.id.authLayoutView, authFragment).commit()
    }

    override fun onAuthenticatorPassed(authenticator: Authenticator) {

        val authFragment: Fragment? = fragmentManager.findFragmentById(R.id.authLayoutView);
        val authChildFragmentManager: FragmentManager? = authFragment!!.childFragmentManager;
        lateinit var authView: AuthenticatorFragment

        when (authenticator.authenticator) {
            Constants.BASIC_AUTH -> {
                authView = authChildFragmentManager!!.findFragmentById(R.id.basicAuthView) as AuthenticatorFragment
            }

            Constants.FIDO -> {
                authView = authChildFragmentManager!!.findFragmentById(R.id.fidoAuthView) as AuthenticatorFragment
            }

            Constants.TOTP_IDP -> {
                authView = authChildFragmentManager!!.findFragmentById(R.id.totpIdpView) as AuthenticatorFragment
            }

            Constants.OPENID -> {
                when (authenticator.idp) {
                    Constants.GOOGLE_IDP -> {
                        authView = authChildFragmentManager!!.findFragmentById(R.id.googleIdpView) as AuthenticatorFragment
                    }
                }
            }
        }

        authView.updateAuthenticator(authenticator);
    }
}