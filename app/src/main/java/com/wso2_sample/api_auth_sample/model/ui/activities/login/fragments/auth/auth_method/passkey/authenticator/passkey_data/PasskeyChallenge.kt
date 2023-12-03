package com.wso2_sample.api_auth_sample.model.ui.activities.login.fragments.auth.auth_method.passkey.authenticator.passkey_data

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.wso2_sample.api_auth_sample.util.Util

data class PasskeyChallenge(
    val challenge: String,
    val allowCredentials: List<String>,
    val timeout: Long,
    val userVerification: String,
    val rpId: String
) {

    companion object {
        fun getPasskeyChallengeFromChallengeString(challengeString: String): PasskeyChallenge {

            val challengeJsonString: String = Util.base64UrlDecode(challengeString)
            val challengeJson: JsonNode = Util.getJsonObject(challengeJsonString)

            val stepTypeReference = object : TypeReference<ChallengeInfo>() {}
            val challengeInfo: ChallengeInfo =
                Util.jsonNodeToObject(challengeJson, stepTypeReference)

            return PasskeyChallenge(
                challenge = challengeInfo.publicKeyCredentialRequestOptions.challenge,
                allowCredentials = emptyList(),
                timeout = 1800000,
                userVerification = "required",
                rpId = challengeInfo.publicKeyCredentialRequestOptions.rpId
            )
        }
    }

    override fun toString(): String {
        return Util.getJsonString(this)
    }

}
