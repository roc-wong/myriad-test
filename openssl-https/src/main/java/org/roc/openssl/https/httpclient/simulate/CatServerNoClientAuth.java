package org.roc.openssl.https.httpclient.simulate;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

/**
 * 详见：https://blog.csdn.net/fw0124/article/details/41013333
 *
 * @author roc
 * @date 2019/1/23 18:12
 */
public class CatServerNoClientAuth implements Runnable, HandshakeCompletedListener {

    public static final int SERVER_PORT = 11123;

    private final Socket _s;

    public CatServerNoClientAuth(Socket s) {
        _s = s;
    }

    public static void main(String[] args) throws Exception {
        String serverKeyStoreFile = "c:\\_tmp\\catserver.keystore";
        String serverKeyStorePwd = "catserverks";
        String catServerKeyPwd = "catserver";

        KeyStore serverKeyStore = KeyStore.getInstance("JKS");
        serverKeyStore.load(new FileInputStream(serverKeyStoreFile), serverKeyStorePwd.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(serverKeyStore, catServerKeyPwd.toCharArray());

        SSLContext sslContext = SSLContext.getInstance("TLSv1");
        sslContext.init(kmf.getKeyManagers(), null, null);

        SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
        SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(SERVER_PORT);
        sslServerSocket.setNeedClientAuth(false);

        while (true) {
            SSLSocket s = (SSLSocket) sslServerSocket.accept();
            CatServerNoClientAuth cs = new CatServerNoClientAuth(s);
            s.addHandshakeCompletedListener(cs);
            new Thread(cs).start();
        }
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(_s.getInputStream()));
            PrintWriter writer = new PrintWriter(_s.getOutputStream(), true);

            writer.println("Welcome~, enter exit to leave.");
            String s;
            while ((s = reader.readLine()) != null && !s.trim().equalsIgnoreCase("exit")) {
                writer.println("Echo: " + s);
            }
            writer.println("Bye~");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                _s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handshakeCompleted(HandshakeCompletedEvent event) {
        try {
            X509Certificate cert = (X509Certificate) event.getPeerCertificates()[0];
        } catch (SSLPeerUnverifiedException ex) {
            System.out.println("handshakeCompleted, SSLPeerUnverified.");
        }
    }
}
