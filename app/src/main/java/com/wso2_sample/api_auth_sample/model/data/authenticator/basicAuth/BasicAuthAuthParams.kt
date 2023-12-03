package com.wso2_sample.api_auth_sample.model.data.authenticator.basicAuth

import com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.AuthParams

/**
 * Basic Auth authenticator parameters data class
 */
data class BasicAuthAuthParams(
    override val username: String,
    override val password: String
): AuthParams(
    username = username,
    password = password
)
