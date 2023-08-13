package com.example.api_auth_sample.util

import com.example.api_auth_sample.model.Authenticator
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class Util {
    companion object {

        const val BASIC_AUTH: String = "BasicAuthenticator";
        const val FIDO: String = "FIDOAuthenticator";
        const val OPENID: String = "OpenIDConnectAuthenticator";

        const val GOOGLE_IDP: String = "google";

        private val mapper: ObjectMapper = jacksonObjectMapper();

        fun getJsonObject(jsonString: String): JsonNode {
            return mapper.readTree(jsonString);
        }

        fun <T> jsonNodeToObject(jsonNode: JsonNode, typeReference: TypeReference<T>): T {
            println(jsonNode);
            return mapper.readValue(jsonNode.toString(), typeReference)
        }
    }
}