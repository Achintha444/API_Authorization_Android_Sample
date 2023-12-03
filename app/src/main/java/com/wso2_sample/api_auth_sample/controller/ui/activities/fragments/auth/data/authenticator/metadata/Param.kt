package com.wso2_sample.api_auth_sample.controller.ui.activities.fragments.auth.data.authenticator.metadata

open class Param (
    /**
     * Param name
     */
    open val param: String?,
    /**
     * Param type
     */
    open val type: String?,
    /**
     * Order of the param
     */
    open val order: Int?,
    /**
     * I18n key of the param
     */
    open val i18nKey: String?,
    /**
     * Display name of the param
     */
    open val displayName: String?,
    /**
     * Is param confidential
     */
    open val confidential: Boolean?
)
