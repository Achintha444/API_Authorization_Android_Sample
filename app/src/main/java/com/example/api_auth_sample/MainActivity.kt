package com.example.api_auth_sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.api_auth_sample.api.APICall
import com.example.api_auth_sample.ui.Factor
import com.example.api_auth_sample.util.UiUtil
import com.fasterxml.jackson.databind.JsonNode

class MainActivity : AppCompatActivity() {

    lateinit var signInButton: Button;
    lateinit var signInLoader: ProgressBar;
    lateinit var layout: View;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        initializeComponents();

        // hide the action bar in the initial screen
        UiUtil.hideActionBar(supportActionBar!!);

        // set on-click listener
        signInButton.setOnClickListener {
            APICall.authorize(
                ::whenAuthentication,
                ::finallyAuthentication,
                ::onAuthenticationSuccess,
                ::onAuthenticationFail
            );
        }
    }

    private fun initializeComponents() {
        setContentView(R.layout.activity_main);
        signInButton = findViewById(R.id.button);
        layout = findViewById(R.id.layout);
        signInLoader = findViewById(R.id.signinLoader);
    }

    private fun onAuthenticationSuccess(authorizeObj: JsonNode) {
        val intent = Intent(this@MainActivity, Factor::class.java);
        intent.putExtra(
            "authenticators",
            authorizeObj["nextStep"]["authenticators"].toString()
        );
        startActivity(intent)
    }

    private fun onAuthenticationFail() {
        UiUtil.showSnackBar(layout, "Sign in Failure");
    }

    private fun whenAuthentication() {
        runOnUiThread {
            signInLoader.visibility = View.VISIBLE;
            signInButton.isEnabled = false;
        }
    }

    private fun finallyAuthentication() {
        runOnUiThread {
            signInLoader.visibility = View.INVISIBLE;
            signInButton.isEnabled = true;
        }
    }
}