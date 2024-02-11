package Init;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

import hilo.LanzarHilo;
import logica.Logica;
import services.GameService;
import services.HtmlService;

public class Principal {

	public static void main(String[] args) {
        try {
        	
    	    Logica logica = new Logica();
    	    HtmlService leerHtmls = new HtmlService();
    		GameService gameService = new GameService();
        	
            // Cargar el almacén de claves
            char[] passphrase = "usuario".toCharArray();
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream("servidor.keystore"), passphrase);

            // Crear el administrador de claves
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, passphrase);

            // Crear el administrador de confianza
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks);

            // Configurar el contexto SSL
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            // Crear el socket del servidor SSL
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
            //SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(443);
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(5000);

            // Esperar por conexiones
            while (true) {
                SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
                LanzarHilo hilo =new LanzarHilo(logica,leerHtmls,gameService,sslSocket);
                Thread LanzarHilo = new Thread(hilo);
                LanzarHilo.start();

                // Procesar la conexión en un nuevo hilo
                //new Thread(new ConnectionHandler(sslSocket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* static class ConnectionHandler implements Runnable {
        private SSLSocket sslSocket;

        public ConnectionHandler(SSLSocket sslSocket) {
            this.sslSocket = sslSocket;
        }

        public void run() {
            try {
                // Configurar el flujo de entrada y salida
                BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
                PrintWriter out = new PrintWriter(sslSocket.getOutputStream(), true);

                // Leer y escribir datos
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Cliente: " + inputLine);
                    //Cliente: Accept-Language
                    if(inputLine.contains("Accept-Language")) {
            	    	break;
            	    }
                    out.println("Respuesta del servidor");
                }
                // Enviar "Hola mundo" al cliente
                out.println("Hola mundo desde el servidor");

                // Cerrar la conexión
                sslSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
}
