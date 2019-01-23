package org.roc.openssl.https.httpclient;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.roc.openssl.https.httpclient.constant.TruststoreConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static com.google.common.truth.Truth.assertThat;

/**
 * @author roc
 * @date 2019/1/23 15:21
 */
public class HttpsOneWayAuthenticationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpsOneWayAuthenticationTest.class);

    public Charset UTF_8 = Charset.forName("UTF-8");

    @Test
    public void testWithTruststore() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException, URISyntaxException {

        SSLContext sslContext = getSSLContext(TruststoreConstant.CUSTOMER_TRUSTSTORE, TruststoreConstant.CUSTOMER_TRUSTSTORE_PASSWORD);

        CloseableHttpClient closeableHttpClient = HttpClients.custom()
                .setSSLContext(sslContext).build();

        HttpGet httpGet = new HttpGet();
        httpGet.setURI(new URI("https://api-dev.ztsrd.com"));
        CloseableHttpResponse httpResponse = closeableHttpClient.execute(httpGet);

        String responseContext = EntityUtils.toString(httpResponse.getEntity(), UTF_8);

        LOGGER.info("response = {}", httpResponse);
        LOGGER.info("responseContext = {}", responseContext);
    }


    public SSLContext getSSLContext(String truststore, String truststorePwd) throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException, KeyManagementException {

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        KeyStore trustKeyStore = KeyStore.getInstance("JKS");

        InputStream truststoreInputStream = this.getClass().getClassLoader().getResourceAsStream(truststore);
        assertThat(truststoreInputStream).isNotNull();
        char[] truststorePassword = truststorePwd != null ? truststorePwd.toCharArray() : null;
        trustKeyStore.load(truststoreInputStream, truststorePassword);
        truststoreInputStream.close();

        trustManagerFactory.init(trustKeyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

        return sslContext;
    }


    @Test
    public void testWithIgnoreTruststore() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException, URISyntaxException {

        TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());

        CloseableHttpClient closeableHttpClient = HttpClients.custom()
                .setSSLContext(sslContext).build();

        HttpGet httpGet = new HttpGet();
        httpGet.setURI(new URI("https://api-dev.ztsrd.com"));
        CloseableHttpResponse httpResponse = closeableHttpClient.execute(httpGet);

        String responseContext = EntityUtils.toString(httpResponse.getEntity(), UTF_8);

        LOGGER.info("response = {}", httpResponse);
        LOGGER.info("responseContext = {}", responseContext);
    }
}
