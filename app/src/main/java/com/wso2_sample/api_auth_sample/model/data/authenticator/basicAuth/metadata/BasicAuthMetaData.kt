package com.wso2_sample.api_auth_sample.model.data.authenticator.basicAuth.metadata

import com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.data.authenticator.metadata.MetaData
import com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.data.authenticator.metadata.Param

data class BasicAuthMetaData(
    /**
     * Prompt type
     */
    override val promptType: String?,
    /**
     * Params
     */
    override var params: ArrayList<Param>?,
): MetaData(
    null,
    promptType,
    params,
    null
)
