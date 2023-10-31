package com.example.api_auth_sample.ui.auth.authMethod

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.api_auth_sample.R
import com.example.api_auth_sample.api.APICall
import com.example.api_auth_sample.api.CustomTrust
import com.example.api_auth_sample.model.AuthParams
import com.example.api_auth_sample.model.Authenticator
import com.example.api_auth_sample.model.AuthenticatorFragment
import com.example.api_auth_sample.ui.Factor
import com.example.api_auth_sample.ui.SignedInInterface
import com.example.api_auth_sample.util.UiUtil
import com.fasterxml.jackson.databind.JsonNode

class BasicAuth : Fragment(), AuthenticatorFragment {

    private lateinit var signingBasicAuth: Button;
    private lateinit var username: EditText;
    private lateinit var password: EditText;
    private lateinit var layout: View;
    override var authenticator: Authenticator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?  {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_basic_auth, container, false)
        initializeComponents(view)

        // set on-click listener
        signingBasicAuth.setOnClickListener {
            APICall.authenticate(
                CustomTrust.getInstance(requireContext()).client,
                requireContext(),
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
        signingBasicAuth = view.findViewById(R.id.signinBasicAuth)
        username = view.findViewById(R.id.username)
        password = view.findViewById(R.id.password)
        layout = view.findViewById(R.id.basicAuthlayout)
    }

    override fun getAuthParams(): AuthParams {
        val usernameText: String = username.text.toString().ifEmpty { "username" }
        val passwordText: String = password.text.toString().ifEmpty { "password" }

        return AuthParams(username = usernameText, password = passwordText)
    }

//    override fun onAuthorizeSuccess(Context context, authorizeObj: JsonNode) {
//        if(authorizeObj["nextStep"] != null) {
//            val intent = Intent(requireActivity(), Factor::class.java);
//            intent.putExtra(
//                "authenticators",
//                authorizeObj["nextStep"]["authenticators"].toString()
//            );
//            startActivity(intent)
//        } else {
//            val intent = Intent(requireActivity(), SignedInInterface::class.java);
//            startActivity(intent)
//        }
//    }

    override fun onAuthorizeFail() {
        UiUtil.showSnackBar(layout, "Sign in Failure");
    }

    override fun whenAuthorizing() {
        requireActivity().runOnUiThread {
            signingBasicAuth.isEnabled = false;
        }
    }

    override fun finallyAuthorizing() {
        requireActivity().runOnUiThread {
            signingBasicAuth.isEnabled = true;
        }
    }
}