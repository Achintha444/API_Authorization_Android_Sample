package com.wso2_sample.api_auth_sample.model.data.authenticator.google

import com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.AuthParams

/**
 * Google authenticator parameters data class
 */
data class GoogleAuthParams(
    override val accessToken: String?,
    override val idToken: String?,
): AuthParams(
    accessToken = accessToken,
    idToken = idToken
)
