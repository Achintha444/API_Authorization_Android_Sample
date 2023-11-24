package com.example.api_auth_sample.model.api.data_source.pet

import com.example.api_auth_sample.model.api.Callback
import com.example.api_auth_sample.model.data.Pet

class GetAllPetsCallback(
    private val onSuccess: (pets: ArrayList<Pet>) -> Unit,
    private val onFailure: (error: Exception) -> Unit,
    private val onWaiting: () -> Unit,
    private val onFinally: () -> Unit
): Callback() {

    fun onSuccess(pets: ArrayList<Pet>) {
        onSuccess.invoke(pets)
    }

    fun onFailure(error: Exception) {
        onFailure.invoke(error)
    }

    override fun onWaiting() {
        onWaiting.invoke()
    }

    override fun onFinally() {
        onFinally.invoke()
    }
}
