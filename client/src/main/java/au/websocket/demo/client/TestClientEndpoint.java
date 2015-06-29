package au.websocket.demo.client;


import au.websocket.demo.common.MsgPackUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;

/**
 * Created by azizunsal on 27/06/15.
 */
@ClientEndpoint
public class TestClientEndpoint {

    private Session userSession;
    private MessageHandler messageHandler;

    final static Logger logger = LoggerFactory.getLogger(TestClientEndpoint.class);

    public TestClientEndpoint(final URI endpointURI) {
        WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();

        try {
            logger.info("Connecting to {}", endpointURI);
            webSocketContainer.connectToServer(this, endpointURI);
            logger.info("Client connected.");
        } catch (DeploymentException | IOException e) {
            logger.error("An error has been occurred while connecting to the server.", e);
            System.exit(-1);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        logger.info("OnOpen:: session is {}", session.getId());
        this.userSession = session;
    }

    @OnMessage
    public void onMessage(ByteBuffer message) {
        logger.info("OnMessage:: message is {}", message);
        if (messageHandler != null) {
            try {
                messageHandler.handleMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void onClose(final Session userSession, final CloseReason closeReason) {
        logger.info("OnClose:: session is {} and the close reason is {} ", userSession, closeReason);
        this.userSession = null;
    }

    @OnError
    public void onError(Throwable t) {
        logger.info("OnError::", t);
    }

    public void sendMessage(String message) {
        logger.info("Sending texts is {}", message);
        try {
            //userSession.getBasicRemote().sendText(message);
            userSession.getBasicRemote().sendBinary(MsgPackUtil.preparePayload(message));
        } catch (IOException e) {
            logger.error("An error has been occurred while sending message to the server.", e);
            closeSession();
        }
    }

    public void addMessageHandler(final MessageHandler msgHandler) {
        logger.debug("Message handler is registered.  The handler is {}", msgHandler);
        this.messageHandler = msgHandler;
    }

    public static interface MessageHandler {
        public void handleMessage(ByteBuffer message) throws IOException;
    }

    public void closeSession() {
        logger.info("Current session is being closed...");
        try {
            userSession.close();
            logger.info("Current session is closed.");
        } catch (IOException e) {
            logger.error("An error has been occurred while closing the session.", e);
        }
    }

}