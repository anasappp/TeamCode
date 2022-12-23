package cn.trunch.weidong.http

import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class HandleSSL{
    fun handleSSLHandshake() {
        try {
            val trustAllCerts: Array<TrustManager> =
                    arrayOf(object : X509TrustManager {
                        val acceptedIssuers: Array<Any?>?
                            get() = arrayOfNulls(0)
                        override fun checkClientTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
                        override fun checkServerTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
                        override fun getAcceptedIssuers(): Array<out X509Certificate>? {
                            return null
                        }
                    })
            val sc: SSLContext = SSLContext.getInstance("TLS")
            // trustAllCerts信任所有的证书
            sc.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory())
            HttpsURLConnection.setDefaultHostnameVerifier { hostname, session -> true }
        } catch (ignored: Exception) {
        }
    }
}