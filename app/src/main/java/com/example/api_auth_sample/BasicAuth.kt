package com.example.api_auth_sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.api_auth_sample.model.Authenticator
import com.example.api_auth_sample.model.AuthenticatorFragment

class BasicAuth : Fragment(), AuthenticatorFragment {

    lateinit var signinBasicAuth: Button;
    override var authenticator: Authenticator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_basic_auth, container, false)
        initializeComponents(view)

        // set on-click listener
        signinBasicAuth.setOnClickListener {
            //APICall.authenticate();
        }

        return view
    }

    private fun initializeComponents(view: View) {
        signinBasicAuth = view.findViewById(R.id.signinBasicAuth);
    }
}