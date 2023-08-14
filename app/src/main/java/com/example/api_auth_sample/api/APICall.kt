package com.example.api_auth_sample.api

import com.example.api_auth_sample.util.Constants
import com.example.api_auth_sample.util.Util
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.io.IOException


class APICall {

    companion object {
        private val client: OkHttpClient = OkHttpClient();
        private const val baseUrl: String = Constants.BaseUrl;

        private fun getUrl(uri: String): HttpUrl.Builder {
            val urlBuilder: HttpUrl.Builder =
                "$baseUrl$uri".toHttpUrlOrNull()!!.newBuilder();
            return urlBuilder;
        }

        @Throws(IOException::class)
        fun authorize(whenAuthorizing: () -> Unit,
            finallyAuthorizing: () -> Unit,
            onSuccessCallback: (authorizeObj: JsonNode) -> Unit,
                      onFailureCallback: () -> Unit) {

            whenAuthorizing();

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
                    finallyAuthorizing();
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    try {
                        // reading the json
                        val model: JsonNode = Util.getJsonObject(response.body!!.string());

                        if(model["ApidogError"] === null) {
                            onSuccessCallback(model);
                        } else {
                            onFailureCallback();
                        }
                    } catch (e: IOException) {
                        println(e);
                        onFailureCallback();
                    } finally {
                        finallyAuthorizing();
                    }
                }
            })
        }
    }
}