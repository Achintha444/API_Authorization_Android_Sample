package com.example.api_auth_sample.api

import com.example.api_auth_sample.api.app_auth_manager.AppAuthManager
import android.content.Context
import android.util.Log
import com.example.api_auth_sample.R
import com.example.api_auth_sample.model.api.app_auth_manager.TokenRequestCallback
import com.example.api_auth_sample.api.cutom_trust_client.CustomTrust
import com.example.api_auth_sample.controller.ui.activities.fragments.auth.AuthController
import com.example.api_auth_sample.model.ui.activities.login.fragments.auth.AuthParams
import com.example.api_auth_sample.model.api.FlowStatus
import com.example.api_auth_sample.model.data.authenticator.Authenticator
import com.example.api_auth_sample.model.util.uiUtil.SharedPreferencesKeys
import com.example.api_auth_sample.util.UiUtil
import com.example.api_auth_sample.util.Util
import com.example.api_auth_sample.util.config.OauthClientConfiguration
import com.fasterxml.jackson.databind.JsonNode
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException

class OauthClient {

    companion object {

        private val client: OkHttpClient = CustomTrust.getInstance().client

        @Throws(IOException::class)
        fun authorize(
            context: Context,
            whenAuthentication: () -> Unit,
            finallyAuthentication: () -> Unit,
            onSuccessCallback: (authorizeObj: JsonNode) -> Unit,
            onFailureCallback: () -> Unit
        ) {

            whenAuthentication()

            // authorize URL
            val url: String = OauthClientConfiguration.getInstance(context).authorizeUri.toString()

            // POST form parameters
            val formBody: RequestBody = FormBody.Builder()
                .add("client_id", OauthClientConfiguration.getInstance(context).clientId)
                .add("response_type", OauthClientConfiguration.getInstance(context).responseType)
                .add("redirect_uri", OauthClientConfiguration.getInstance(context).redirectUri.toString())
                .add("state", OauthClientConfiguration.getInstance(context).state)
                .add("scope", OauthClientConfiguration.getInstance(context).scope)
                .add("response_mode", OauthClientConfiguration.getInstance(context).responseMode)
                .build()

            val request: Request = Request.Builder().url(url).post(formBody).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println(e)
                    onFailureCallback()
                    finallyAuthentication()
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    try {
                        // reading the json
                        val model: JsonNode = Util.getJsonObject(response.body!!.string())

                        onSuccessCallback(model)
                    } catch (e: Exception) {
                        println(e)
                        onFailureCallback()
                    } finally {
                        finallyAuthentication()
                    }
                }
            })
        }

        @Throws(IOException::class)
        fun authenticate(
            context: Context,
            authenticator: Authenticator,
            authParams: AuthParams,
            whenAuthentication: () -> Unit,
            finallyAuthentication: () -> Unit,
            onSuccessCallback: (authorizeObj: JsonNode) -> Unit,
            onFailureCallback: () -> Unit
        ) {

            val flowId: String = UiUtil.readFromSharedPreferences(
                context.getSharedPreferences(
                    R.string.app_name.toString(), Context.MODE_PRIVATE
                ), SharedPreferencesKeys.FLOW_ID.key
            ).toString()

            whenAuthentication()

            // authorize next URL
            val url: String = OauthClientConfiguration.getInstance(context).authorizeNextUri.toString()

            val request: Request = Request.Builder().url(url)
                .post(AuthController.buildRequestBodyForAuth(flowId, authenticator, authParams))
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println(e)
                    onFailureCallback()
                    finallyAuthentication()
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    try {
                        if (response.code == 200) {
                            // reading the json
                            val model: JsonNode = Util.getJsonObject(response.body!!.string())

                            // Assessing the flow status
                            val flowStatusNode: JsonNode? = model["flowStatus"]
                            val flowStatus: String =
                                if (flowStatusNode != null && flowStatusNode.isTextual) {
                                    flowStatusNode.asText()
                                } else {
                                    // Handle the case when "flowStatus" is null or not a valid string
                                    FlowStatus.SUCCESS.flowStatus
                                }

                            when(flowStatus) {
                                 FlowStatus.FAIL_INCOMPLETE.flowStatus -> {
                                     onFailureCallback()
                                 }
                                FlowStatus.INCOMPLETE.flowStatus -> {
                                    onSuccessCallback(model)
                                }
                                FlowStatus.SUCCESS.flowStatus -> {
                                    token(context, model, onSuccessCallback, onFailureCallback)
                                }
                            }
                        } else {
                            onFailureCallback()
                        }
                    } catch (e: IOException) {
                        println(e)
                        onFailureCallback()
                    } finally {
                        finallyAuthentication()
                    }
                }
            })
        }

        fun token(
            context: Context,
            model: JsonNode?,
            onSuccessCallback: (authorizeObj: JsonNode) -> Unit,
            onFailureCallback: () -> Unit
        ) {

            val appAuthManager = AppAuthManager(context)
            val authorizationCode: String = model!!.get("code").asText()

            appAuthManager.exchangeAuthorizationCodeForAccessToken(authorizationCode, TokenRequestCallback(
                onSuccess = { accessToken: String ->
                    UiUtil.writeToSharedPreferences(
                        context.getSharedPreferences(R.string.app_name.toString(),
                            Context.MODE_PRIVATE), SharedPreferencesKeys.ACCESS_TOKEN.key, accessToken)
                    onSuccessCallback(model)
                },
                onFailure = { error: java.lang.Exception ->
                    Log.e("Token request failed", error.toString())
                    onFailureCallback()
                }
            ))
        }
    }
}