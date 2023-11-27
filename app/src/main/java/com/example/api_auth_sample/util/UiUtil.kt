package com.example.api_auth_sample.util

import android.content.SharedPreferences
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.os.Build
import androidx.appcompat.app.ActionBar
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray

class UiUtil {

    companion object {
        fun showSnackBar(layout: View, message: String) {
            val snackBar: Snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG)
            snackBar.show()
        }

        fun hideActionBar(supportActionBar: ActionBar) {
            try {
                supportActionBar.hide()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun writeToSharedPreferences(sharedPreferences: SharedPreferences, key: String, value: String) {
            with(sharedPreferences.edit()) {
                putString(key, value)
                apply()
            }
        }

        fun readFromSharedPreferences(sharedPreferences: SharedPreferences, key: String): String? {
            return sharedPreferences.getString(key, null)
        }

        fun hideStatusBar(window: Window, resources: Resources, theme: Theme, color: Int) {
            // Fullscreen mode
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.statusBarColor = resources.getColor(color, theme)
            } else {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
        }

        /**
         * Serialize a list of objects to a JSON array
         * @param list The list of objects to serialize
         */
        fun serializeList(list: List<Any>): RequestBody {
            val jsonArray = JSONArray(list)

            // Create a JSON request body
            val jsonMediaType: MediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
                ?: throw IllegalArgumentException("serializeList: Invalid media type")

            return jsonArray.toString().toRequestBody(jsonMediaType)
        }
    }

}