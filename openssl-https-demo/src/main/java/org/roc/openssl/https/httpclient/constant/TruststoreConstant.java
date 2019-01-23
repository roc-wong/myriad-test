package org.roc.openssl.https.httpclient.constant;

/**
 * @author roc
 * @date 2019/1/22 15:56
 */
public class TruststoreConstant {

    /**
     * 客户端P12证书，可使用{@code openssl pkcs12 -export}生成
     */
    public static final String CLIENT_CERT = "client.p12";


    /**
     * 客户端P12证书密码，使用{@code openssl pkcs12 -export}生成时指定的密码
     */
    public static final String CLIENT_CERT_PASSWORD = "";


    /**
     * 由根证书ca.crt生成个人的信任证书库，命令：
     *
     * {@code keytool -keystore roc.truststore -storepass sunshine -alias api-dev -import -trustcacerts -file ca.cer}
     *
     */
    public static final String CUSTOMER_TRUSTSTORE = "roc.truststore";


    /**
     * 信任证书库访问密码
     *
     */
    public static final String CUSTOMER_TRUSTSTORE_PASSWORD = "sunshine";

}
