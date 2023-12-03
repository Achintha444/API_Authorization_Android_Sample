package com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.data.authenticator

import com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.data.authenticator.metadata.MetaData

open class Authenticator (
    /**
     * Authenticator id
     */
    open val authenticatorId: String,
    /**
     * Authenticator type
     */
    open val authenticator: String,
    /**
     * Idp type
     */
    open val idp: String,
    /**
     * Authenticator metadata
     */
    open val metadata: MetaData?,
    /**
     * Authenticator required parameters
     */
    open val requiredParams: List<String>?
)
