# API Authentication Android Sample - WSO2 Identity Server

## ⚠️ Read this first

1. Required versions
 ```
 Java version >= 17
 ```
 ```
 Android Studio version >= Giraffe
 ```

## Setup steps

1. Setup the latest version of WSO2 Identity Sever.
2. Create a mobile application.
1. Create `config.xml` file in `res/values`

```
<resources>
    <string name="authorize_url">https://10.0.2.2:9443/oauth2/authorize</string>
    <string name="authorize_next_url">https://10.0.2.2:9443/oauth2/authn</string>
    <string name="client_id"> <CLIENT_ID> </string>
    <string name="redirect_uri"> <REDIRECT_URI> </string>
    <string name="scope">openid internal_login</string>
    <string name="response_type">code</string>
    <string name="state">random123</string>
    <string name="response_mode">direct</string>
    <string name="google_web_client_id"> <WSO2_CLIENT_ID_OF_GOOGLE> </string>
</resources>
```

#### (Assuming you are using an emulator) We need to add the certificate of WSO2 identity server to our app
4. Create a new keystore with CN as localhost and SAN as 10.0.2.2
```
keytool -genkey -alias wso2carbon -keyalg RSA -keystore wso2carbon.jks -keysize 2048 -ext SAN=IP:10.0.2.2
```
6. Export the public certificate (name it as wso2carbon.pem)to add into the truststore.
```
keytool -exportcert -alias wso2carbon -keystore wso2carbon.jks -rfc -file wso2carbon.pem
```
7. Import the certificate in the client-truststore.jks file located in `<IS_HOME>/repository/resources/security/`
```
keytool -import -alias wso2is -file wso2carbon.pem -keystore client-truststore.jks -storepass wso2carbon
```
8. Now copy this public certificate (wso2carbon.pem) into the `res/raw` folder.

#### Setup Google Login
9. Go to `https://console.cloud.google.com/` and create a new project.
10. In the `credentials` section create two `Oauth Client IDs` one for `android app(Select Android)` and one for `WSO2 identity server(Select Web Application)`.
> Follow the steps to get the SHA-1 key to create Oauth Client ID for our Android app
> https://stackoverflow.com/a/33479550/10601286

> `WSO2_CLIENT_ID_OF_GOOGLE` is the client ID of the client ID crated for the WSO2 identity server.

11. Create a Google connection as a Trusted Token Issuer using the following curl command.
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
12. Add the created Google connection to the created application (in step 2) as a level 1 sign-in option.
