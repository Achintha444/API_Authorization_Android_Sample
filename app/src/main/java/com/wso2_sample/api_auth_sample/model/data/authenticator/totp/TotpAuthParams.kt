package com.wso2_sample.api_auth_sample.model.data.authenticator.totp

import com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.AuthParams

/**
 * TOTP authenticator parameters data class
 */
data class TotpAuthParams(
    override val totp: String
): AuthParams(
    totp = totp
)
