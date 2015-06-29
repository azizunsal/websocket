package au.websocket.demo.client;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;
import org.msgpack.type.Value;
import org.msgpack.unpacker.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.List;
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
        client.addMessageHandler(message -> {
            logger.info("Response from server is {}", message);

            MessagePack msgpack = new MessagePack();

            // Deserialize directly using a template
            List<String> dst1 = msgpack.read(message, Templates.tList(Templates.TString));
            logger.info("MessagePack Field -1 {}", dst1.get(0));
            logger.info("MessagePack Field -2 {}", dst1.get(1));
            logger.info("MessagePack Field -3 {}", dst1.get(2));

            logger.info("MessagePack as a whole {}", msgpack);

        });

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
