package org.roc.openssl.https.httpclient;

import com.google.common.truth.Truth;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.roc.openssl.https.httpclient.constant.TruststoreConstant;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import static com.google.common.truth.Truth.assertThat;

/**
 * @author roc
 * @date 2019/1/23 13:54
 */
public class HttpsTwoFactorAuthenticationTest {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(HttpsTwoFactorAuthenticationTest.class);
    public Charset UTF_8 = Charset.forName("UTF-8");

    @Test
    public void testWithCaCerts() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException, KeyManagementException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(TruststoreConstant.CLIENT_CERT);
        Truth.assertThat(inputStream).isNotNull();
        char[] password = TruststoreConstant.CLIENT_CERT_PASSWORD != null ? TruststoreConstant.CLIENT_CERT_PASSWORD.toCharArray() : null;
        keyStore.load(inputStream, password);
        inputStream.close();

        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, password, null).build();

        CloseableHttpClient httpclient = HttpClients.custom().setSSLContext(sslcontext).build();

        String url = "https://api-dev.ztsrd.com";

        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String responseContext = EntityUtils.toString(response.getEntity(), "UTF-8");
        EntityUtils.consume(entity);

        LOGGER.info("responseContext = {}", responseContext);

        response.close();
    }


    @Test
    public void testWithTruststore() throws URISyntaxException, IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        SSLContext sslContext = getSSLContext(TruststoreConstant.CLIENT_CERT, TruststoreConstant.CLIENT_CERT_PASSWORD,
                TruststoreConstant.NGINX_TRUSTSTORE, TruststoreConstant.CUSTOMER_TRUSTSTORE_PASSWORD);

        CloseableHttpClient closeableHttpClient = HttpClients.custom()
                .setSSLContext(sslContext).build();

        HttpGet httpGet = new HttpGet();
        httpGet.setURI(new URI("https://api-dev.ztsrd.com"));
        CloseableHttpResponse httpResponse = closeableHttpClient.execute(httpGet);

        String responseContext = EntityUtils.toString(httpResponse.getEntity(), UTF_8);

        LOGGER.info("response = {}", httpResponse);
        LOGGER.info("responseContext = {}", responseContext);
    }


    public SSLContext getSSLContext(String clientCert, String clientCertPwd, String truststore, String truststorePwd) throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException, KeyManagementException {

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        InputStream clientCertInputStream = this.getClass().getClassLoader().getResourceAsStream(clientCert);
        assertThat(clientCertInputStream).isNotNull();
        char[] password = clientCertPwd != null ? clientCertPwd.toCharArray() : null;
        keyStore.load(clientCertInputStream, password);
        clientCertInputStream.close();

        keyManagerFactory.init(keyStore, password);

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        KeyStore trustKeyStore = KeyStore.getInstance("JKS");

        InputStream truststoreInputStream = this.getClass().getClassLoader().getResourceAsStream(truststore);
        assertThat(truststoreInputStream).isNotNull();
        char[] truststorePassword = truststorePwd != null ? truststorePwd.toCharArray() : null;
        trustKeyStore.load(truststoreInputStream, truststorePassword);
        truststoreInputStream.close();

        trustManagerFactory.init(trustKeyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

        return sslContext;
    }


    @Test
    public void testWithTruststoreNative() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {

        SSLContext sslContext = getSSLContext(TruststoreConstant.CLIENT_CERT, TruststoreConstant.CLIENT_CERT_PASSWORD,
                TruststoreConstant.NGINX_TRUSTSTORE, TruststoreConstant.CUSTOMER_TRUSTSTORE_PASSWORD);

        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        URL url = new URL("https://api-dev.ztsrd.com");
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
//        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
//        con.setRequestProperty("Accept-Language", "zh-CN;en-US,en;q=0.5");
        urlConnection.setRequestMethod("GET");

        InputStream inputStream = urlConnection.getInputStream();
        Truth.assertThat(inputStream).isNotNull();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));

        StringBuilder responseBuilder = new StringBuilder();
        String buff;
        while ((buff = bufferedReader.readLine()) != null) {
            responseBuilder.append(buff).append("\n");
        }

        inputStream.close();

        LOGGER.info("response = {}", responseBuilder.toString());
    }

}