package com.wso2_sample.api_auth_sample.ui.activities.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.wso2_sample.api_auth_sample.R
import com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.auth_method.AuthenticatorFragment
import com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.data.authenticator.Authenticator
import com.wso2_sample.api_auth_sample.databinding.ActivityLoginBinding
import com.wso2_sample.api_auth_sample.model.ui.activities.login.fragments.auth.auth_method.basic_auth.authenticator.BasicAuthAuthenticator
import com.wso2_sample.api_auth_sample.model.ui.activities.login.fragments.auth.auth_method.google.authenticator.GoogleAuthenticator
import com.wso2_sample.api_auth_sample.model.ui.activities.login.fragments.auth.auth_method.passkey.authenticator.PasskeyAuthenticator
import com.wso2_sample.api_auth_sample.model.ui.activities.login.fragments.auth.auth_method.totp.authenticator.TotpAuthenticator
import com.wso2_sample.api_auth_sample.ui.activities.login.fragments.auth.AuthFragment
import com.wso2_sample.api_auth_sample.ui.activities.login.fragments.auth.auth_method.BasicAuth
import com.wso2_sample.api_auth_sample.ui.activities.login.fragments.auth.auth_method.GoogleFragment
import com.wso2_sample.api_auth_sample.ui.activities.login.fragments.auth.auth_method.PasskeyFragment
import com.wso2_sample.api_auth_sample.ui.activities.login.fragments.auth.auth_method.totp.TotpFragment
import com.wso2_sample.api_auth_sample.util.UiUtil

class Login : AppCompatActivity(), AuthFragment.AuthListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var stepString: String
    private lateinit var flowId: String
    private lateinit var fragmentManager: FragmentManager
    private lateinit var authFragment: AuthFragment
    private lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setAuthenticatorsString()

        initalizeAuthFragment()

        initializeComponents()

        // hide action bar and status bar
        UiUtil.hideStatusBar(window, resources, theme, R.color.asgardeo_secondary)
    }

    private fun initializeComponents() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setAuthenticatorsString() {
        val intent: Intent = intent
        stepString = intent.getStringExtra("step")!!
        flowId = intent.getStringExtra("flowId")!!
    }

    private fun initalizeAuthFragment() {
        fragmentManager = supportFragmentManager
        val mFragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        authFragment = AuthFragment()

        bundle = Bundle()
        bundle.putString("stepString", stepString)
        bundle.putString("flowId", flowId)

        authFragment.arguments = bundle
        mFragmentTransaction.add(R.id.authLayoutView, authFragment).commit()
    }

    override fun onAuthenticatorPassed(authenticator: Authenticator) {

        val authFragment: Fragment? = fragmentManager.findFragmentById(R.id.authLayoutView);
        val authChildFragmentManager: FragmentManager = authFragment!!.childFragmentManager;
        lateinit var authView: AuthenticatorFragment

        when (authenticator.authenticator) {
            BasicAuthAuthenticator.AUTHENTICATOR_TYPE -> {
                authView =
                    authChildFragmentManager.findFragmentById(R.id.basicAuthView) as BasicAuth
                authView.updateAuthenticator(authenticator)
            }

            PasskeyAuthenticator.AUTHENTICATOR_TYPE -> {
                authView =
                    authChildFragmentManager.findFragmentById(R.id.passkeyAuthView) as PasskeyFragment
                authView.updateAuthenticator(authenticator.convertToPasskeyAuthenticator())
            }

            TotpAuthenticator.AUTHENTICATOR_TYPE -> {
                authView =
                    authChildFragmentManager.findFragmentById(R.id.totpIdpView) as TotpFragment
                authView.updateAuthenticator(authenticator)
            }

            GoogleAuthenticator.AUTHENTICATOR_TYPE -> {
                authView =
                    authChildFragmentManager.findFragmentById(R.id.googleIdpView) as GoogleFragment
                authView.updateAuthenticator(authenticator)
            }
        }
    }
}