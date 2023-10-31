package com.example.api_auth_sample.util

object Constants {

    const val BaseUrl: String = "https://10.100.8.100:9443";
    const val APIDogId: String = "3863493"; // 3863493, 3864484
    const val APIDogAuthId: String = "3863759"; // 3864485 3863759
    const val BASIC_AUTH: String = "BasicAuthenticator";
    const val FIDO: String = "FIDOAuthenticator";
    const val OPENID: String = "OpenIDConnectAuthenticator";
    const val GOOGLE_OPENID: String = "GoogleOIDCAuthenticator"
    const val GOOGLE_IDP: String = "google";
    const val TOTP_IDP: String = "totp";
    const val LOCAL_IDP: String = "LOCAL";
}