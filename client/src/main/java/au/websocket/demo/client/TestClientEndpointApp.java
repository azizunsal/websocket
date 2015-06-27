package au.websocket.demo.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

/**
 * Created by azizunsal on 27/06/15.
 */
public class TestClientEndpointApp {

    final static Logger logger = LoggerFactory.getLogger(TestClientEndpointApp.class);
    final static String endpointURI = "ws://localhost:8080/server/test";

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {

        logger.info("WebSocket client app started!");

        TestClientEndpoint client = new TestClientEndpoint(URI.create(endpointURI));
        client.addMessageHandler(message -> logger.info("Response from server is {}", message));

        boolean running = true;
        Scanner sc = new Scanner(System.in);
        while (running) {
            System.out.println("Enter your message > ");
            String msg = sc.nextLine();
            if (msg.equals("quit")) running = false;
            client.sendMessage(msg);

        }
        logger.info("WebSocket client stopped.");
    }
}
