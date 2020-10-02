package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.message.play.player.AdvancementTabMessage;

public class AdvancementTabCodec implements Codec<AdvancementTabMessage> {

    @Override
    public AdvancementTabMessage decode(ByteBuf buffer) throws IOException {
        int action = ByteBufUtils.readVarInt(buffer);
        if (action == AdvancementTabMessage.ACTION_OPEN) {
            String tabId = ByteBufUtils.readUTF8(buffer);
            return new AdvancementTabMessage(action, tabId);
        }
        return new AdvancementTabMessage();
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, AdvancementTabMessage message) throws IOException {
        ByteBufUtils.writeVarInt(buffer, message.getAction());
        if (message.getAction() == AdvancementTabMessage.ACTION_OPEN) {
            ByteBufUtils.writeUTF8(buffer, message.getTabId());
        }
        return buffer;
    }
}
