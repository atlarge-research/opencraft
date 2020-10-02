package science.atlarge.opencraft.opencraft.net.codec.status;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.message.status.StatusResponseMessage;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public final class StatusResponseCodec implements Codec<StatusResponseMessage> {

    @Override
    public StatusResponseMessage decode(ByteBuf buffer) throws IOException {
        String json = ByteBufUtils.readUTF8(buffer);
        return new StatusResponseMessage((JSONObject) JSONValue.parse(json));
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, StatusResponseMessage message) throws IOException {
        ByteBufUtils.writeUTF8(buffer, message.getJson());
        return buffer;
    }
}
