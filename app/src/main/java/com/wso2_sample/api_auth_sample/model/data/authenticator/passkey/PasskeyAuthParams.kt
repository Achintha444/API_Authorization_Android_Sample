package com.wso2_sample.api_auth_sample.model.data.authenticator.passkey

import com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.AuthParams

/**
 * Passkey authenticator parameters data class
 */
data class PasskeyAuthParams(
    override val tokenResponse: String
): AuthParams(
    tokenResponse = tokenResponse
)
