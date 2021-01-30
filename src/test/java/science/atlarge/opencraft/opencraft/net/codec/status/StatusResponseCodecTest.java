package science.atlarge.opencraft.opencraft.net.codec.status;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.status.StatusResponseMessage;
import org.json.simple.JSONObject;

public class StatusResponseCodecTest extends CodecTest<StatusResponseMessage> {

    @Override
    protected Codec<StatusResponseMessage> createCodec() {
        return new StatusResponseCodec();
    }

    @Override
    protected StatusResponseMessage createMessage() {
        JSONObject object = new JSONObject();
        return new StatusResponseMessage(object);
    }
}
