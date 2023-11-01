package com.example.api_auth_sample.model

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignInClient

class GoogleSignInActivityResultContract(googleSignInClient: GoogleSignInClient)
    : ActivityResultContract<Unit, Intent>() {

    private var mGoogleSignInClient: GoogleSignInClient

    init {
        mGoogleSignInClient = googleSignInClient
    }

    override fun createIntent(context: Context, input: Unit): Intent {
        return mGoogleSignInClient.signInIntent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Intent {
        return intent!!
    }
}
