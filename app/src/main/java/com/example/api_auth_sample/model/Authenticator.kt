package com.example.api_auth_sample.model

data class Authenticator (
    val authenticator: String,
    val idp: String,
    val metadata: Any?
)
