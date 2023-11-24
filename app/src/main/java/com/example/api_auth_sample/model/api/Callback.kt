package com.example.api_auth_sample.model.api

/**
 * Abstract class to be used as a callback for API requests.
 */
abstract class Callback {
    /**
     * Called when the request is waiting for a response.
     */
    abstract fun onWaiting()

    /**
     * Called when the request is finished.
     */
    abstract fun onFinally()
}
