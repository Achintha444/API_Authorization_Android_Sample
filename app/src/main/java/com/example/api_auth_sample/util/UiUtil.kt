package com.example.api_auth_sample.util

import androidx.appcompat.app.ActionBar
import android.view.View
import com.google.android.material.snackbar.Snackbar

class UiUtil {

    companion object {
        fun showSnackBar(layout: View, message: String) {
            val snackBar: Snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG)
            snackBar.show()
        }

        fun hideActionBar(supportActionBar: ActionBar) {
            supportActionBar.hide();
        }
    }

}