package au.websocket.demo.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * Created by azizunsal on 27/06/15.
 */

@ServerEndpoint(value = "/test")
public class TestServerEndpoint {
    final static Logger logger = LoggerFactory.getLogger(TestServerEndpoint.class);

    @OnOpen
    public void onOpen(Session session) {
        logger.debug("Session {} opened a connection.", session.getId());

        try {
            session.getBasicRemote().sendText("Connection established");
        } catch (IOException e) {
            logger.error("An error has been occurred while sending response to client.", e);
            closeSession(session);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("{} received from {}", message, session.getId());
        try {
            session.getBasicRemote().sendText("Message received.");
        } catch (IOException e) {
            logger.error("An error has been occurred while sending response to the client.", e);
            closeSession(session);
        }
    }

    @OnError
    public void onError(Throwable t) {
        logger.error("An error has been occurred.", t);
    }

    private void closeSession(Session session) {
        try {
            session.close();
        } catch (IOException e) {
            logger.error("An error has been occurred while closing the session", e);
        }
    }
}
