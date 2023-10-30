package com.example.api_auth_sample.api

import com.example.api_auth_sample.controller.AuthController
import com.example.api_auth_sample.model.AuthParams
import com.example.api_auth_sample.util.Constants
import com.example.api_auth_sample.util.Util
import com.fasterxml.jackson.databind.JsonNode
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.io.IOException


class APICall {

    companion object {
        private val client: OkHttpClient = OkHttpClient();
        private const val baseUrl: String = Constants.BaseUrl;

        private fun getUrl(uri: String): HttpUrl.Builder {
            return "$baseUrl$uri".toHttpUrlOrNull()!!.newBuilder();
        }

        @Throws(IOException::class)
        fun authorize(
            whenAuthentication: () -> Unit,
            finallyAuthentication: () -> Unit,
            onSuccessCallback: (authorizeObj: JsonNode) -> Unit,
            onFailureCallback: () -> Unit
        ) {

            whenAuthentication();

            // authorize URL
            val urlBuilder: HttpUrl.Builder = getUrl("/oauth2/authorize");
            urlBuilder.addQueryParameter("apidogApiId", Constants.APIDogId);

            val url: String = urlBuilder.build().toString();

            // test POST body.
            // Values are not required as this is mocked.
            val formBody: RequestBody = FormBody.Builder().build();

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

                        if (model["ApidogError"] === null) {
                            onSuccessCallback(model);
                        } else {
                            onFailureCallback();
                        }
                    } catch (e: IOException) {
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
            authenticator: com.example.api_auth_sample.model.Authenticator,
            authParams: AuthParams,
            whenAuthentication: () -> Unit,
            finallyAuthentication: () -> Unit,
            onSuccessCallback: (authorizeObj: JsonNode) -> Unit,
            onFailureCallback: () -> Unit
        ) {

            whenAuthentication();

            // authorize URL
            val urlBuilder: HttpUrl.Builder = getUrl("/api/authenticate/v1");
            urlBuilder.addQueryParameter("apidogApiId", Constants.APIDogAuthId);

            val url: String = urlBuilder.build().toString();

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

                        println(model);

                        if (model["ApidogError"] === null) {
                            onSuccessCallback(model);
                        } else {
                            onFailureCallback();
                        }
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