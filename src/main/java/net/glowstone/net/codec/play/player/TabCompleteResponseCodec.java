package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.glowstone.net.message.play.player.TabCompleteResponseMessage;

public final class TabCompleteResponseCodec implements Codec<TabCompleteResponseMessage> {

    @Override
    public TabCompleteResponseMessage decode(ByteBuf buffer) throws IOException {
        int count = ByteBufUtils.readVarInt(buffer);
        List<String> completions = new ArrayList<>(count);
        for (int index = 0; index < count; index++) {
            String completion = ByteBufUtils.readUTF8(buffer);
            completions.add(completion);
        }
        return new TabCompleteResponseMessage(completions);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, TabCompleteResponseMessage message) throws IOException {
        ByteBufUtils.writeVarInt(buffer, message.getCompletions().size());
        for (String completion : message.getCompletions()) {
            ByteBufUtils.writeUTF8(buffer, completion);
        }
        return buffer;
    }
}
