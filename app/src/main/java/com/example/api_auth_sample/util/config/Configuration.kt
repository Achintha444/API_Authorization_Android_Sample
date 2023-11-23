package com.example.api_auth_sample.util.config

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.example.api_auth_sample.R
import java.lang.ref.WeakReference

/**
 * Reads and validates the configuration from the res/values/config file.
 */
class Configuration private constructor(context: Context) {
    private val mResources: Resources
    private var mBaseUri: Uri? = null
    private var mAuthorizeUri: Uri? = null
    private var mAuthorizeNextUri: Uri? = null
    private var mTokenUri: Uri? = null
    private var mClientId: String? = null
    private var mRedirectUri: Uri? = null
    private var mScope: String? = null
    private var mState: String? = null
    private var mResponseMode: String? = null
    private var mResponseType: String? = null
    private var mGoogleWebClientId: String? = null

    init {
        mResources = context.resources
        readConfiguration()
    }

    val authorizeUri: Uri
        /**
         * Returns the authorize endpoint URI specified in the res/values/config file.
         *
         * @return Authorize URI.
         */
        get() = mAuthorizeUri!!

    val authorizeNextUri: Uri
        /**
         * Returns the next authorize endpoint URI specified in the res/values/config file.
         *
         * @return Next Authorize URI.
         */
        get() = mAuthorizeNextUri!!

    val tokenUri: Uri
        /**
         * Returns the token endpoint URI specified in the res/values/config file.
         *
         * @return Token URI.
         */
        get() = mTokenUri!!

    val clientId: String
        /**
         * Returns the client id specified in the res/values/config file.
         *
         * @return Client ID.
         */
        get() = mClientId!!
    val redirectUri: Uri
        /**
         * Returns the redirect URI specified in the res/values/config file.
         *
         * @return Redirect URI.
         */
        get() = mRedirectUri!!

    val scope: String
        /**
         * Returns the scope specified in the res/values/config file.
         *
         * @return Scope.
         */
        get() = mScope!!

    val state: String
        /**
         * Returns the state specified in the res/values/config file.
         *
         * @return State.
         */
        get() = mState!!

    val responseMode: String
        /**
         * Returns the response mode specified in the res/values/config file.
         *
         * @return Response Mode.
         */
        get() = mResponseMode!!

    val responseType: String
        /**
         * Returns the response type specified in the res/values/config file.
         *
         * @return Response Type.
         */
        get() = mResponseType!!

    val googleWebClientId: String
        /**
         * Returns the google web client id specified in the res/values/config file.
         *
         * @return Google Web Client ID.
         */
        get() = mGoogleWebClientId!!

    /**
     * Reads the configuration values.
     */
    private fun readConfiguration() {

        mBaseUri = getRequiredUri(mResources.getString(R.string.base_url))

        mAuthorizeUri = getRequiredUri(mBaseUri.toString() + "/oauth2/authorize")
        mAuthorizeNextUri = getRequiredUri(mBaseUri.toString() + "/oauth2/authn")
        mTokenUri = getRequiredUri(mBaseUri.toString() + "/oauth2/token")
        mClientId = getRequiredConfigString(mResources.getString(R.string.client_id))
        mRedirectUri = getRequiredUri(mResources.getString(R.string.redirect_uri))
        mScope = getRequiredConfigString(mResources.getString(R.string.scope))
        mState = getRequiredConfigString(mResources.getString(R.string.state))
        mResponseMode = getRequiredConfigString(mResources.getString(R.string.response_mode))
        mResponseType = getRequiredConfigString(mResources.getString(R.string.response_type))
        mGoogleWebClientId = getRequiredConfigString(mResources.getString(R.string.google_web_client_id))
    }

    /**
     * Returns the Config String of the the particular property name.
     *
     * @param configString Property name.
     * @return Property value.
     */
    private fun getRequiredConfigString(configString: String): String? {
        var value: String? = configString
        if (value != null) {
            if (TextUtils.isEmpty(value)) {
                value = null
            }
        }
        if(value == null){
            Log.e(
                LOG_TAG,
                "value is required for getRequiredConfigString but not specified"
            )
        }
        return value
    }

    /**
     * Returns Config URI.
     *
     * @param endpoint Endpoint
     * @return Uri
     */
    @Throws(Exception::class)
    private fun getRequiredUri(endpoint: String): Uri {
        val uri = Uri.parse(endpoint)
        if (!uri.isHierarchical || !uri.isAbsolute) {
            throw Exception("$endpoint must be hierarchical and absolute")
        }
        if (!TextUtils.isEmpty(uri.encodedUserInfo)) {
            throw Exception("$endpoint must not have user info")
        }
        if (!TextUtils.isEmpty(uri.encodedQuery)) {
            throw Exception("$endpoint must not have query parameters")
        }
        if (!TextUtils.isEmpty(uri.encodedFragment)) {
            throw Exception("$endpoint must not have a fragment")
        }
        return uri
    }

    companion object {
        private var sInstance = WeakReference<Configuration?>(null)
        private const val LOG_TAG = "Configuration"

        /**
         * Returns an instance of the FileBasedConfiguration class.
         *
         * @param context Context object with information about the current state of the application.
         * @return FileBasedConfiguration instance.
         */
        fun getInstance(context: Context): Configuration {
            var config = sInstance.get()
            if (config == null) {
                config = Configuration(context)
                sInstance = WeakReference(config)
            }
            return config
        }
    }
}