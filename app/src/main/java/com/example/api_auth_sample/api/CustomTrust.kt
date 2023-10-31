package com.example.api_auth_sample.api

import com.example.api_auth_sample.R
import android.content.Context
import com.example.api_auth_sample.util.config.Configuration
import okhttp3.OkHttpClient
import okhttp3.Protocol
import java.io.IOException
import java.io.InputStream
import java.lang.ref.WeakReference
import java.security.GeneralSecurityException
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.util.Arrays
import java.util.concurrent.TimeUnit
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class CustomTrust private constructor(private val context: Context) {
    val client: OkHttpClient

    companion object {
        private var sInstance = WeakReference<CustomTrust?>(null)

        /**
         * Returns an instance of the FileBasedConfiguration class.
         *
         * @param context Context object with information about the current state of the application.
         * @return FileBasedConfiguration instance.
         */
        fun getInstance(context: Context): CustomTrust {
            var customTrust = sInstance.get()
            if (customTrust == null) {
                customTrust = CustomTrust(context)
                sInstance = WeakReference(customTrust)
            }
            return customTrust
        }
    }

    init {
        val trustManager: X509TrustManager
        val sslSocketFactory: SSLSocketFactory
        try {
            trustManager = trustManagerForCertificates(trustedCertificatesInputStream())
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, arrayOf<TrustManager>(trustManager), null)
            sslSocketFactory = sslContext.socketFactory
        } catch (e: GeneralSecurityException) {
            throw RuntimeException(e)
        }
        client = OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustManager)
            .connectTimeout(45, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .protocols(listOf<Protocol>(Protocol.HTTP_1_1))
            .build()
    }

    /**
     * Returns an input stream containing the certificate PEM file. This implementation just
     * embeds the PEM file in Java strings; most applications will instead read this from a resource
     * file that gets bundled with the application.
     */
    private fun trustedCertificatesInputStream(): InputStream {
        return context.resources.openRawResource(R.raw.wso2carbon)
    }

    /**
     * Returns a trust manager that trusts `certificate` and none other. HTTPS services whose
     * certificates have not been signed by these certificates will fail with a `SSLHandshakeException`.
     */
    @Throws(GeneralSecurityException::class)
    private fun trustManagerForCertificates(`in`: InputStream): X509TrustManager {
        val certificateFactory = CertificateFactory.getInstance("X.509")
        val certificates = certificateFactory.generateCertificates(`in`)
        if (certificates.isEmpty()) {
            throw IllegalArgumentException("expected non-empty set of trusted certificates")
        }

        // Put the certificates a key store.
        val password = "password".toCharArray() // Any password will work.
        val keyStore = newEmptyKeyStore(password)
        var index = 0
        for (certificate: Certificate in certificates) {
            val certificateAlias = (index++).toString()
            keyStore.setCertificateEntry(certificateAlias, certificate)
        }

        // Use it to build an X509 trust manager.
        val keyManagerFactory = KeyManagerFactory.getInstance(
            KeyManagerFactory.getDefaultAlgorithm()
        )
        keyManagerFactory.init(keyStore, password)
        val trustManagerFactory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        )
        trustManagerFactory.init(keyStore)
        val trustManagers = trustManagerFactory.trustManagers
        if (trustManagers.size != 1 || trustManagers.get(0) !is X509TrustManager) {
            throw IllegalStateException(
                "Unexpected default trust managers:"
                        + Arrays.toString(trustManagers)
            )
        }
        return trustManagers[0] as X509TrustManager
    }

    @Throws(GeneralSecurityException::class)
    private fun newEmptyKeyStore(password: CharArray): KeyStore {
        try {
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            val `in`: InputStream? = null // By convention, 'null' creates an empty key store.
            keyStore.load(`in`, password)
            return keyStore
        } catch (e: IOException) {
            throw AssertionError(e)
        }
    }
}