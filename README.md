# API Authentication Android Sample - WSO2 Identity Server

## ⚠️ Read this first

1. Required versions
 ```
 Java version >= 17
 ```
 ```
 Android Studio version >= Giraffe
 ```

#### Setup steps Identity Server
1. Setup the latest version of WSO2 Identity Sever.
2. Create a mobile application.

#### Import the configurations
3. Create `config.xml` file in `res/values`

```
<resources>
    <string name="oauth_client_base_url"> <BASE_URL> </string>
    <string name="oauth_client_client_id"> <CLIENT_ID> </string>
    <string name="oauth_client_redirect_uri">https://example-app.com/redirect</string>
    <string name="oauth_client_scope">openid internal_login</string>
    <string name="oauth_client_response_type">code</string>
    <string name="oauth_client_state">random123</string>
    <string name="oauth_client_response_mode">direct</string>
    <string name="oauth_client_google_web_client_id"> <WSO2_CLIENT_ID_OF_GOOGLE> </string>
    <string name="data_source_resource_server_url"> <PET_RESOURCE_SERVER_URL> </string>
</resources>
```

#### Setup Google Login
4. Go to `https://console.cloud.google.com/` and create a new project.
5. In the `credentials` section create two `Oauth Client IDs` one for `android app(Select Android)` and one for `WSO2 identity server(Select Web Application)`.
> Follow the steps to get the SHA-1 key to create Oauth Client ID for our Android app
> https://stackoverflow.com/a/67983215/10601286

> `WSO2_CLIENT_ID_OF_GOOGLE` is the client ID of the client ID crated for the WSO2 identity server.

6. Create a Google connection as a Trusted Token Issuer using the following curl command.
```
curl --location 'https://localhost:9443/api/server/v1/identity-providers' \
--header 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/118.0' \
--header 'Accept: application/json' \
--header 'Accept-Language: en-US,en;q=0.5' \
--header 'Accept-Encoding: gzip, deflate, br' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YWRtaW46YWRtaW4=' \
--data '{
    "image": "assets/images/logos/google.svg",
    "isPrimary": false,
    "roles": {
        "mappings": [],
        "outboundProvisioningRoles": []
    },
    "name": "Google-TTI-IDP",
    "certificate": {
        "certificates": [],
        "jwksUri": "https://www.googleapis.com/oauth2/v3/certs"
    },
    "claims": {
        "userIdClaim": {
            "uri": "http://wso2.org/claims/username"
        },
        "roleClaim": {
            "uri": "http://wso2.org/claims/role"
        },
        "provisioningClaims": []
    },
    "description": "Login users with existing Google accounts.",
    "alias": <WSO2_CLIENT_ID_OF_GOOGLE>,
    "homeRealmIdentifier": "",
    "provisioning": {
        "jit": {
            "userstore": "DEFAULT",
            "scheme": "PROVISION_SILENTLY",
            "isEnabled": true
        }
    },
    "federatedAuthenticators": {
        "defaultAuthenticatorId": "R29vZ2xlT0lEQ0F1dGhlbnRpY2F0b3I",
        "authenticators": [
            {
                "isEnabled": true,
                "authenticatorId": "R29vZ2xlT0lEQ0F1dGhlbnRpY2F0b3I",
                "properties": [
                    {
                        "value": <WSO2_CLIENT_ID_OF_GOOGLE>,
                        "key": "ClientId"
                    },
                    {
                        "value": <WSO2_CLIENT_SECRET_OF_GOOGLE>,
                        "key": "ClientSecret"
                    },
                    {
                        "value": "https://localhost:9443/commonauth",
                        "key": "callbackUrl"
                    },
                    {
                        "value": "scope=email openid profile",
                        "key": "AdditionalQueryParameters"
                    }
                ]
            }
        ]
    },
    "idpIssuerName": "https://accounts.google.com",
    "isFederationHub": false,
    "templateId": "google-idp"
}'
```
7. Add the created Google connection to the created application (in step 2) as a level 1 sign-in option.
