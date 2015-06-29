package au.websocket.demo.server;

import au.websocket.demo.common.MsgPackUtil;
import org.msgpack.MessagePack;
import org.msgpack.template.Templates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

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
            session.getBasicRemote().sendBinary(MsgPackUtil.preparePayload("Connection established"));
        } catch (IOException e) {
            logger.error("An error has been occurred while sending response to client.", e);
            closeSession(session);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("{} received from {}", message, session.getId());
        try {
            session.getBasicRemote().sendText("Resp from the server.");
        } catch (IOException e) {
            logger.error("An error has been occurred while sending response to the client.", e);
            closeSession(session);
        }
    }

    @OnMessage
    public void onMessage(ByteBuffer message, Session session) {
        logger.info("Binary message {} received from {}", message, session.getId());

        MessagePack msgpack = new MessagePack();

        try {
            // Deserialize directly using a template
            List<String> dst1 = msgpack.read(message, Templates.tList(Templates.TString));
            logger.info("MessagePack Field -1 {}", dst1.get(0));
            logger.info("MessagePack Field -2 {}", dst1.get(1));
            logger.info("MessagePack Field -3 {}", dst1.get(2));

            logger.info("MessagePack as a whole {}", msgpack);

            session.getBasicRemote().sendBinary(MsgPackUtil.preparePayload("Resp from the server."));
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
