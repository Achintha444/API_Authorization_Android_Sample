package com.example.api_auth_sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.api_auth_sample.api.OauthClient
import com.example.api_auth_sample.ui.Factor
import com.example.api_auth_sample.util.UiUtil
import com.fasterxml.jackson.databind.JsonNode

class MainActivity : AppCompatActivity() {

    private lateinit var signInLoader: ProgressBar
    private lateinit var retrySiginButton: Button
    lateinit var layout: View;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        initializeComponents();

        // hide action bar and status bar
        UiUtil.hideActionBar(supportActionBar!!)
        UiUtil.hideStatusBar(window, resources, theme, R.color.asgardeo_secondary)

        retrySignInButtonOnClick()
    }

    private fun initializeComponents() {
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.layout)
        retrySiginButton = findViewById(R.id.retrySigin)
        signInLoader = findViewById(R.id.signinLoader)
    }

    private fun retrySignInButtonOnClick() {
        retrySiginButton.setOnClickListener {
            OauthClient.authorize(
                applicationContext,
                ::whenAuthentication,
                ::finallyAuthentication,
                ::onAuthenticationSuccess,
                ::onAuthenticationFail
            );
        }
    }

    private fun onAuthenticationSuccess(authorizeObj: JsonNode) {
        val intent = Intent(this@MainActivity, Factor::class.java);
        intent.putExtra(
            "flowId",
            authorizeObj["flowId"].toString()
        );
        intent.putExtra(
            "step",
            authorizeObj["nextStep"].toString()
        );
        startActivity(intent)
    }

    private fun onAuthenticationFail() {
        UiUtil.showSnackBar(layout, "Sign in Failure");
        runOnUiThread {
            retrySiginButton.visibility = View.VISIBLE;
        }
    }

    private fun whenAuthentication() {
        runOnUiThread {
            signInLoader.visibility = View.VISIBLE;
        }
    }

    private fun finallyAuthentication() {
        runOnUiThread {
            signInLoader.visibility = View.INVISIBLE;
        }
    }
}