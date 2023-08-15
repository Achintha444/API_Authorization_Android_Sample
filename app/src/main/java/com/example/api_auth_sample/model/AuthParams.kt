package com.example.api_auth_sample.model

data class AuthParams (
    val username: String? = null,
    val password: String? = null,
    val code: String? = null,
    val state: String? = null,
    val otp: String? = null,
    val tokenResponse: String? = null
)
