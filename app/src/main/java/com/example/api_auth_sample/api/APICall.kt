package com.example.api_auth_sample.api

import android.content.Context
import com.example.api_auth_sample.controller.AuthController
import com.example.api_auth_sample.model.AuthParams
import com.example.api_auth_sample.util.Constants
import com.example.api_auth_sample.util.Util
import com.example.api_auth_sample.util.config.Configuration
import com.fasterxml.jackson.databind.JsonNode
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException
import javax.net.ssl.SSLContext


class APICall {

    companion object {
        @Throws(IOException::class)
        fun authorize(
            client: OkHttpClient,
            context: Context,
            whenAuthentication: () -> Unit,
            finallyAuthentication: () -> Unit,
            onSuccessCallback: (authorizeObj: JsonNode) -> Unit,
            onFailureCallback: () -> Unit
        ) {

            whenAuthentication();

            // authorize URL
            val url: String = Configuration.getInstance(context).authorizeUri.toString()

            // POST form parameters
            val formBody: RequestBody = FormBody.Builder()
                .add("client_id", Configuration.getInstance(context).clientId)
                .add("response_type", Configuration.getInstance(context).responseType)
                .add("redirect_uri", Configuration.getInstance(context).redirectUri.toString())
                .add("state", Configuration.getInstance(context).state)
                .add("scope", Configuration.getInstance(context).scope)
                .add("response_mode", Configuration.getInstance(context).responseMode)
                .build();

            val request: Request = Request.Builder().url(url).post(formBody).build();

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println(e);
                    onFailureCallback();
                    finallyAuthentication();
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    try {
                        // reading the json
                        val model: JsonNode = Util.getJsonObject(response.body!!.string());

                        onSuccessCallback(model);
                    } catch (e: Exception) {
                        println(e);
                        onFailureCallback();
                    } finally {
                        finallyAuthentication();
                    }
                }
            })
        }

        @Throws(IOException::class)
        fun authenticate(
            client: OkHttpClient,
            context: Context,
            authenticator: com.example.api_auth_sample.model.Authenticator,
            authParams: AuthParams,
            whenAuthentication: () -> Unit,
            finallyAuthentication: () -> Unit,
            onSuccessCallback: (context: Context, authorizeObj: JsonNode) -> Unit,
            onFailureCallback: () -> Unit
        ) {

            whenAuthentication();

            // authorize next URL
            val url: String = Configuration.getInstance(context).authorizeNextUri.toString();

            val request: Request = Request.Builder().url(url)
                .post(AuthController.buildRequestBodyForAuth(authenticator, authParams)).build();

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println(e);
                    onFailureCallback();
                    finallyAuthentication();
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    try {
                        // reading the json
                        val model: JsonNode = Util.getJsonObject(response.body!!.string());

                        onSuccessCallback(context, model);
                    } catch (e: IOException) {
                        println(e);
                        onFailureCallback();
                    } finally {
                        finallyAuthentication();
                    }
                }
            })
        }
    }
}