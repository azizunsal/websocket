package au.websocket.demo.common;

import org.msgpack.MessagePack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by azizunsal on 29/06/15.
 */
public class MsgPackUtil {

    public static ByteBuffer preparePayload(String message) throws IOException {
        // Create serialize objects.
        List<String> src = new ArrayList<String>();
        src.add("Real Message => " + message);
        src.add("dummy-field-" + Math.random());
        src.add("dummy-field-" + Math.random());

        MessagePack msgpack = new MessagePack();
        // Serialize
        byte[] raw = msgpack.write(src);
        ByteBuffer payload = ByteBuffer.wrap(raw);
        return payload;
    }
}
