package com.wso2_sample.api_auth_sample.model.data.authenticator.passkey.metadata

import com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.data.authenticator.metadata.MetaData
import com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.data.authenticator.metadata.Param

data class PasskeyMetaData(
    /**
     * I18n key of the param
     */
    override val i18nKey: String?,
    /**
     * Prompt type
     */
    override val promptType: String?,
    /**
     * Params
     */
    override val params: ArrayList<Param>?,
    /**
     * Additional data
     */
    override val additionalData: PasskeyAdditionalData
): MetaData(
    i18nKey,
    promptType,
    params,
    additionalData
)
