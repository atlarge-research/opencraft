package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.glowstone.net.message.play.player.AdvancementTabMessage;

public class AdvancementTabCodec implements Codec<AdvancementTabMessage> {

    @Override
    public AdvancementTabMessage decode(ByteBuf buffer) throws IOException {
        int action = ByteBufUtils.readVarInt(buffer);
        if (action == AdvancementTabMessage.ACTION_CLOSE) {
            return new AdvancementTabMessage();
        }
        String tabId = ByteBufUtils.readUTF8(buffer);
        return new AdvancementTabMessage(action, tabId);
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
