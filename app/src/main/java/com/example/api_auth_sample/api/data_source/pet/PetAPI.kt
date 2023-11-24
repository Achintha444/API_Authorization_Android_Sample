package com.example.api_auth_sample.api.data_source.pet

import android.content.Context
import com.example.api_auth_sample.api.cutom_trust_client.CustomTrust
import com.example.api_auth_sample.model.api.data_source.pet.GetAllPetsCallback
import com.example.api_auth_sample.model.data.Pet
import com.example.api_auth_sample.util.config.DataSourceConfiguration
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class PetAPI {

    companion object {

        private val client: OkHttpClient = CustomTrust.getInstance().client

        @Throws(IOException::class)
        fun getPets(
            context: Context,
            accessToken: String,
            callback: GetAllPetsCallback
        ) {

            callback.onWaiting()

            // authorize URL
            val url: String = DataSourceConfiguration.getInstance(context).resourceServerUrl.toString() + "/pets"


            val requestBuilder: Request.Builder = Request.Builder().url(url)
            requestBuilder.addHeader("Authorization", "Bearer $accessToken")

            client.newCall(requestBuilder.build()).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println(e)
                    callback.onFailure(e)
                    callback.onFinally()
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    try {
                        // reading the json
                        val pets: ArrayList<Pet> = jacksonObjectMapper().readValue(
                            response.body!!.string(),
                            jacksonObjectMapper().typeFactory.constructCollectionType(
                                ArrayList::class.java,
                                Pet::class.java
                            )
                        )
                        callback.onSuccess(pets)
                    } catch (e: Exception) {
                        println(e)
                        callback.onFailure(e)
                    } finally {
                        callback.onFinally()
                    }
                }
            })
        }
    }
}