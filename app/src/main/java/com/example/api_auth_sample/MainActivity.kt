package com.example.api_auth_sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.api_auth_sample.api.APICall
import com.example.api_auth_sample.util.UiUtil
import com.fasterxml.jackson.databind.JsonNode

class MainActivity : AppCompatActivity() {

    lateinit var signInButton: Button;
    lateinit var layout: View;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        initializeComponents();

        // hide the action bar in the initial screen
        UiUtil.hideActionBar(supportActionBar!!);

        // set on-click listener
        signInButton.setOnClickListener {
            APICall.authorize(::onAuthorizeSuccess, ::onAuthorizeFail);
        }
    }

    private fun initializeComponents() {
        setContentView(R.layout.activity_main);
        signInButton = findViewById<Button>(R.id.button);
        layout = findViewById<View>(R.id.layout);
    }

    private fun onAuthorizeSuccess(authorizeObj: JsonNode) {
        val intent = Intent(this@MainActivity, FirstFactor::class.java);
        intent.putExtra("authenticators",
            authorizeObj["currentStep"]["authenticators"].toString());
        startActivity(intent)
    }

    private fun onAuthorizeFail() {
        println("fail");
        UiUtil.showSnackBar(layout, "Sign in Failure");
    }

}